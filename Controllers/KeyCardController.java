package Controllers;

import Models.AccessLevels;
import Models.Booking;
import Models.KeyCard;
import Storage.StorageHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KeyCardController {

    private StorageHelper keyCardStorage;
    private final String KEY_CARD_STORE = "key_cards";

    public KeyCardController(String baseDirectory) throws IOException {
        this.keyCardStorage = new StorageHelper(baseDirectory, KEY_CARD_STORE);
    }

    public void createUpdateKeyCard(KeyCard keyCard) throws IOException {
        keyCardStorage.getStore(KEY_CARD_STORE).save(keyCard.getCardNumber(), keyCard.toMap());
    }

    public KeyCard getKeyCard(String cardNumber) throws IOException {
        Map<String, Object> data = keyCardStorage.getStore(KEY_CARD_STORE).load(cardNumber);
        return data != null ? new KeyCard().fromMap(data) : null;
    }

    public List<KeyCard> getAllKeyCards() throws IOException {
        List<KeyCard> keyCards = new ArrayList<>();
        keyCardStorage.getStore(KEY_CARD_STORE).loadAll().forEach(map -> {
            keyCards.add(new KeyCard().fromMap(map));
        });
        return keyCards;
    }

    public void newKeyCard(Booking booking) throws IOException {
        KeyCard keyCard = new KeyCard(AccessLevels.BASIC, String.valueOf(getAllKeyCards().size() + 1),
                booking.getId(), booking.getCustomerId());
        createUpdateKeyCard(keyCard);
    }



}
