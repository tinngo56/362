package Storage;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class StorageHelper {

    private final String baseDirectory;
    private final Map<String, DataStore<?>> stores;
    private final JsonConverter jsonConverter;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public StorageHelper(String baseDirectory, String storeName) throws IOException {
        this.baseDirectory = baseDirectory;
        this.stores = new HashMap<>();
        this.jsonConverter = new JsonConverter();
        initializeDirectories();
        initializeStores(storeName);
    }

    private void initializeDirectories() throws IOException {
        Files.createDirectories(Paths.get(baseDirectory));
    }

    public void initializeStores(String storeName) throws IOException {
        registerStore(new BasicStore<>(storeName));
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
        private final Map<String, Map<String, Object>> hashMap;

        public BasicStore(String storeName) {
            this.storeName = storeName;
            this.hashMap = new HashMap<>();
        }

        @Override
        public String getStoreName() {
            return storeName;
        }

        @Override
        public void save(String id, Map<String, Object> data) throws IOException {
            data.put("lastModified", LocalDateTime.now().format(DATE_FORMAT));
            hashMap.put(id, new HashMap<>(data));
            Path filePath = getFilePath(id);
            jsonConverter.writeToFile(data, filePath);
        }

        @Override
        public Map<String, Object> load(String id) throws IOException {
            if (hashMap.containsKey(id)) {
                return new HashMap<>(hashMap.get(id));
            }

            Path filePath = getFilePath(id);
            if (!Files.exists(filePath)) {
                return null;
            }

            Map<String, Object> data = jsonConverter.readFromFile(filePath);
            hashMap.put(id, new HashMap<>(data));
            return data;
        }

        @Override
        public List<Map<String, Object>> loadAll() throws IOException {
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
        }

        @Override
        public void delete(String id) throws IOException {
            hashMap.remove(id);
            Files.deleteIfExists(getFilePath(id));
        }

        private Path getFilePath(String id) {
            return Paths.get(baseDirectory, storeName, id + ".json");
        }
    }

    public class JsonConverter {
        private static final String INDENT = "  ";

        public void writeToFile(Map<String, Object> data, Path path) throws IOException {
            String json = mapToJson(data, 0);
            Files.write(path, json.getBytes());
        }

        public Map<String, Object> readFromFile(Path path) throws IOException {
            String content = new String(Files.readAllBytes(path));
            return parseJson(content);
        }

        private String mapToJson(Map<String, Object> map, int depth) {
            StringBuilder json = new StringBuilder();
            String indent = INDENT.repeat(depth);
            json.append("{\n");

            Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                json.append(indent).append(INDENT)
                        .append("\"").append(escape(entry.getKey())).append("\": ")
                        .append(valueToJson(entry.getValue(), depth + 1));

                if (it.hasNext()) {
                    json.append(",");
                }
                json.append("\n");
            }

            json.append(indent).append("}");
            return json.toString();
        }

        private String valueToJson(Object value, int depth) {
            if (value == null) {
                return "null";
            }

            if (value instanceof String) {
                return "\"" + escape((String) value) + "\"";
            }

            if (value instanceof Number || value instanceof Boolean) {
                return value.toString();
            }

            if (value instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) value;
                return mapToJson(map, depth);
            }

            if (value instanceof List || value.getClass().isArray()) {
                List<?> list;
                if (value.getClass().isArray()) {
                    list = Arrays.asList((Object[]) value);
                } else {
                    list = (List<?>) value;
                }
                return listToJson(list, depth);
            }

            if (value instanceof LocalDateTime) {
                return "\"" + value.toString() + "\"";
            }

            // For any other object types, convert to string
            return "\"" + escape(value.toString()) + "\"";
        }

        private String listToJson(List<?> list, int depth) {
            if (list.isEmpty()) {
                return "[]";
            }

            StringBuilder json = new StringBuilder();
            String indent = INDENT.repeat(depth);
            json.append("[\n");

            for (int i = 0; i < list.size(); i++) {
                json.append(indent).append(INDENT)
                        .append(valueToJson(list.get(i), depth + 1));

                if (i < list.size() - 1) {
                    json.append(",");
                }
                json.append("\n");
            }

            json.append(indent).append("]");
            return json.toString();
        }

        private String escape(String s) {
            if (s == null) {
                return "";
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                char ch = s.charAt(i);
                switch (ch) {
                    case '"':
                        sb.append("\\\"");
                        break;
                    case '\\':
                        sb.append("\\\\");
                        break;
                    case '\b':
                        sb.append("\\b");
                        break;
                    case '\f':
                        sb.append("\\f");
                        break;
                    case '\n':
                        sb.append("\\n");
                        break;
                    case '\r':
                        sb.append("\\r");
                        break;
                    case '\t':
                        sb.append("\\t");
                        break;
                    default:
                        if (ch < ' ') {
                            String hex = String.format("\\u%04x", (int) ch);
                            sb.append(hex);
                        } else {
                            sb.append(ch);
                        }
                }
            }
            return sb.toString();
        }

        public Map<String, Object> parseJson(String json) {
            JsonParser parser = new JsonParser(json);
            return parser.parse();
        }

        private class JsonParser {
            private final String json;
            private int pos = 0;

            public JsonParser(String json) {
                this.json = json.trim();
            }

            public Map<String, Object> parse() {
                return parseObject();
            }

            private Map<String, Object> parseObject() {
                Map<String, Object> map = new HashMap<>();

                consumeWhitespace();
                if (!consume('{')) {
                    throw new IllegalStateException("Expected '{' at position " + pos);
                }

                while (true) {
                    consumeWhitespace();
                    if (peek() == '}') {
                        pos++;
                        break;
                    }

                    // Parse the key
                    String key = parseString();

                    consumeWhitespace();
                    if (!consume(':')) {
                        throw new IllegalStateException("Expected ':' at position " + pos);
                    }

                    // Parse the value
                    consumeWhitespace();
                    Object value = parseValue();
                    map.put(key, value);

                    consumeWhitespace();
                    char c = peek();
                    if (c == '}') {
                        pos++;
                        break;
                    }
                    if (!consume(',')) {
                        throw new IllegalStateException("Expected ',' or '}' at position " + pos);
                    }
                }

                return map;
            }

            private List<Object> parseArray() {
                List<Object> list = new ArrayList<>();

                if (!consume('[')) {
                    throw new IllegalStateException("Expected '[' at position " + pos);
                }

                while (true) {
                    consumeWhitespace();
                    if (peek() == ']') {
                        pos++;
                        break;
                    }

                    list.add(parseValue());

                    consumeWhitespace();
                    char c = peek();
                    if (c == ']') {
                        pos++;
                        break;
                    }
                    if (!consume(',')) {
                        throw new IllegalStateException("Expected ',' or ']' at position " + pos);
                    }
                }

                return list;
            }

            private Object parseValue() {
                consumeWhitespace();
                char c = peek();

                if (c == '"') {
                    return parseString();
                }
                if (c == '{') {
                    return parseObject();
                }
                if (c == '[') {
                    return parseArray();
                }
                if (Character.isDigit(c) || c == '-') {
                    return parseNumber();
                }
                if (c == 't' && json.startsWith("true", pos)) {
                    pos += 4;
                    return true;
                }
                if (c == 'f' && json.startsWith("false", pos)) {
                    pos += 5;
                    return false;
                }
                if (c == 'n' && json.startsWith("null", pos)) {
                    pos += 4;
                    return null;
                }

                throw new IllegalStateException("Unexpected character at position " + pos);
            }

            private String parseString() {
                if (!consume('"')) {
                    throw new IllegalStateException("Expected '\"' at position " + pos);
                }

                StringBuilder sb = new StringBuilder();
                boolean escaped = false;

                while (pos < json.length()) {
                    char c = json.charAt(pos++);

                    if (escaped) {
                        switch (c) {
                            case '"':
                            case '\\':
                            case '/':
                                sb.append(c);
                                break;
                            case 'b':
                                sb.append('\b');
                                break;
                            case 'f':
                                sb.append('\f');
                                break;
                            case 'n':
                                sb.append('\n');
                                break;
                            case 'r':
                                sb.append('\r');
                                break;
                            case 't':
                                sb.append('\t');
                                break;
                            case 'u':
                                if (pos + 4 > json.length()) {
                                    throw new IllegalStateException("Incomplete Unicode escape at position " + pos);
                                }
                                String hex = json.substring(pos, pos + 4);
                                sb.append((char) Integer.parseInt(hex, 16));
                                pos += 4;
                                break;
                            default:
                                sb.append(c);
                        }
                        escaped = false;
                    } else if (c == '\\') {
                        escaped = true;
                    } else if (c == '"') {
                        return sb.toString();
                    } else {
                        sb.append(c);
                    }
                }

                throw new IllegalStateException("Unterminated string at position " + pos);
            }

            private Number parseNumber() {
                int start = pos;
                boolean isFloat = false;

                // Handle negative numbers
                if (peek() == '-') {
                    pos++;
                }

                // Parse digits
                while (pos < json.length() && (Character.isDigit(peek()) || peek() == '.')) {
                    if (peek() == '.') {
                        isFloat = true;
                    }
                    pos++;
                }

                String num = json.substring(start, pos);
                try {
                    return isFloat ? Double.parseDouble(num) : Long.parseLong(num);
                } catch (NumberFormatException e) {
                    throw new IllegalStateException("Invalid number at position " + start + ": " + num);
                }
            }

            private void consumeWhitespace() {
                while (pos < json.length() && Character.isWhitespace(json.charAt(pos))) {
                    pos++;
                }
            }

            private boolean consume(char expected) {
                if (pos < json.length() && json.charAt(pos) == expected) {
                    pos++;
                    return true;
                }
                return false;
            }

            private char peek() {
                return pos < json.length() ? json.charAt(pos) : '\0';
            }
        }
    }
}