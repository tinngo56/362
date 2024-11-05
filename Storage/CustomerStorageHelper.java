package Storage;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class CustomerStorageHelper {
    private final String baseDirectory;
    private final Map<String, DataStore<?>> stores;
    private final JsonConverter jsonConverter;
    private static final DateTimeFormatter DATE_FORMAT = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public CustomerStorageHelper(String baseDirectory) throws IOException {
        this.baseDirectory = baseDirectory;
        this.stores = new HashMap<>();
        this.jsonConverter = new JsonConverter();
        initializeDirectories();
        initializeStores();
    }

    private void initializeDirectories() throws IOException {
        Files.createDirectories(Paths.get(baseDirectory));
    }

    private void initializeStores() throws IOException {
        // Initialize core stores
        registerStore(new BasicStore<>("customers"));
    }

    private void registerStore(DataStore<?> store) throws IOException {
        stores.put(store.getStoreName(), store);
        Files.createDirectories(Paths.get(baseDirectory, store.getStoreName()));
    }

    @SuppressWarnings("unchecked")
    public <T> DataStore<T> getStore(String storeName) {
        DataStore<T> store = (DataStore<T>) stores.get(storeName);
        if (store == null) {
            throw new IllegalArgumentException("Store not found: " + storeName);
        }
        return store;
    }

    // Core interfaces
    public interface DataStore<T> {
        String getStoreName();
        void save(String id, Map<String, Object> data) throws IOException;
        Map<String, Object> load(String id) throws IOException;
        List<Map<String, Object>> loadAll() throws IOException;
        void delete(String id) throws IOException;
    }

    // Basic store implementation
    private class BasicStore<T> implements DataStore<T> {
        private final String storeName;
        private final ReentrantReadWriteLock lock;
        private final Map<String, Map<String, Object>> cache;

        public BasicStore(String storeName) {
            this.storeName = storeName;
            this.lock = new ReentrantReadWriteLock();
            this.cache = new HashMap<>();
        }

        @Override
        public String getStoreName() {
            return storeName;
        }

        @Override
        public void save(String id, Map<String, Object> data) throws IOException {
            lock.writeLock().lock();
            try {
                data.put("lastModified", LocalDateTime.now().format(DATE_FORMAT));
                cache.put(id, new HashMap<>(data));
                Path filePath = getFilePath(id);
                jsonConverter.writeToFile(data, filePath);
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public Map<String, Object> load(String id) throws IOException {
            lock.readLock().lock();
            try {
                if (cache.containsKey(id)) {
                    return new HashMap<>(cache.get(id));
                }

                Path filePath = getFilePath(id);
                if (!Files.exists(filePath)) {
                    return null;
                }

                Map<String, Object> data = jsonConverter.readFromFile(filePath);
                cache.put(id, new HashMap<>(data));
                return data;
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public List<Map<String, Object>> loadAll() throws IOException {
            lock.readLock().lock();
            try {
                List<Map<String, Object>> results = new ArrayList<>();
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(
                        Paths.get(baseDirectory, storeName), "*.json")) {
                    for (Path path : stream) {
                        String id = path.getFileName().toString().replace(".json", "");
                        Map<String, Object> data = load(id);
                        if (data != null) {
                            results.add(data);
                        }
                    }
                }
                return results;
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public void delete(String id) throws IOException {
            lock.writeLock().lock();
            try {
                cache.remove(id);
                Files.deleteIfExists(getFilePath(id));
            } finally {
                lock.writeLock().unlock();
            }
        }

        private Path getFilePath(String id) {
            return Paths.get(baseDirectory, storeName, id + ".json");
        }
    }

    // JSON conversion utility
    private static class JsonConverter {
        public void writeToFile(Map<String, Object> data, Path path) throws IOException {
            String json = mapToJson(data);
            Files.write(path, json.getBytes());
        }

        public Map<String, Object> readFromFile(Path path) throws IOException {
            String content = new String(Files.readAllBytes(path));
            return jsonToMap(content);
        }

        private String mapToJson(Map<String, Object> map) {
            StringBuilder json = new StringBuilder("{\n");
            Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                json.append("  \"").append(escape(entry.getKey())).append("\": ");
                json.append(valueToJson(entry.getValue()));
                if (it.hasNext()) {
                    json.append(",");
                }
                json.append("\n");
            }
            json.append("}");
            return json.toString();
        }

        private String valueToJson(Object value) {
            if (value == null) {
                return "null";
            } else if (value instanceof String) {
                return "\"" + escape((String) value) + "\"";
            } else if (value instanceof Number || value instanceof Boolean) {
                return value.toString();
            } else if (value instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) value;
                return mapToJson(map);
            } else if (value instanceof List) {
                return listToJson((List<?>) value);
            } else if (value instanceof LocalDateTime) {
                return "\"" + ((LocalDateTime) value).format(DATE_FORMAT) + "\"";
            }
            return "\"" + escape(value.toString()) + "\"";
        }

        private String listToJson(List<?> list) {
            StringBuilder json = new StringBuilder("[\n");
            for (int i = 0; i < list.size(); i++) {
                json.append("  ").append(valueToJson(list.get(i)));
                if (i < list.size() - 1) {
                    json.append(",");
                }
                json.append("\n");
            }
            json.append("]");
            return json.toString();
        }

        private Map<String, Object> jsonToMap(String json) {
            Map<String, Object> map = new HashMap<>();
            json = json.trim();
            if (!json.startsWith("{") || !json.endsWith("}")) {
                throw new IllegalArgumentException("Invalid JSON object format");
            }
            
            json = json.substring(1, json.length() - 1).trim();
            String[] pairs = json.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            
            for (String pair : pairs) {
                pair = pair.trim();
                String[] keyValue = pair.split(":", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim().replaceAll("\"", "");
                    String value = keyValue[1].trim();
                    map.put(key, parseValue(value));
                }
            }
            
            return map;
        }

        private Object parseValue(String value) {
            value = value.trim();
            if (value.equals("null")) {
                return null;
            } else if (value.startsWith("\"") && value.endsWith("\"")) {
                return unescape(value.substring(1, value.length() - 1));
            } else if (value.equals("true")) {
                return true;
            } else if (value.equals("false")) {
                return false;
            } else if (value.startsWith("{")) {
                return jsonToMap(value);
            } else if (value.startsWith("[")) {
                return parseArray(value);
            } else {
                try {
                    if (value.contains(".")) {
                        return Double.parseDouble(value);
                    } else {
                        return Long.parseLong(value);
                    }
                } catch (NumberFormatException e) {
                    return value;
                }
            }
        }

        private List<Object> parseArray(String json) {
            List<Object> list = new ArrayList<>();
            json = json.substring(1, json.length() - 1).trim();
            if (json.isEmpty()) {
                return list;
            }
            
            String[] elements = json.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            for (String element : elements) {
                list.add(parseValue(element.trim()));
            }
            
            return list;
        }

        private String escape(String s) {
            return s.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
        }

        private String unescape(String s) {
            return s.replace("\\\\", "\\")
                    .replace("\\\"", "\"")
                    .replace("\\n", "\n")
                    .replace("\\r", "\r")
                    .replace("\\t", "\t");
        }
    }

    // Query interface
    public interface Query<T> {
        boolean matches(Map<String, Object> record);
    }

    // Query execution method
    public <T> List<Map<String, Object>> query(String storeName, Query<T> query) throws IOException {
        DataStore<T> store = getStore(storeName);
        return store.loadAll().stream()
                .filter(query::matches)
                .collect(Collectors.toList());
    }
}