package root.storygram.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

@Service
public class UniqueIdService {

    public String generateUniqueId() {
        return String.valueOf(UUID.randomUUID());
    }

    public String generateUniqueShortId() {

        UUID uuid = UUID.randomUUID();
        byte[] uuidBytes = toBytes(uuid);

        return Base64.getUrlEncoder().withoutPadding().encodeToString(uuidBytes);
    }



    private byte[] toBytes(UUID uuid) {
        byte[] bytes = new byte[16];
        ByteBuffer.wrap(bytes)
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits());
        return bytes;
    }
}
