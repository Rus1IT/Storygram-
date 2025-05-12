package root.storygram.service;

public class FileService {

    public static String getExtension(String contentType) {
        String extension;
        if ("image/png".equals(contentType)) {
            extension = "png";
        } else if ("image/jpeg".equals(contentType)) {
            extension = "jpg";
        } else if ("video/mp4".equals(contentType)) {
            extension = "mp4";
        } else if ("video/webm".equals(contentType)) {
            extension = "webm";
        } else {
            throw new IllegalArgumentException("Unsupported content type: " + contentType);
        }
        return extension;
    }

    public static String getImageExtension(String contentType) {
        String extension;
        if ("image/png".equals(contentType)) {
            extension = "png";
        } else if ("image/jpeg".equals(contentType)) {
            extension = "jpg";
        } else {
            throw new IllegalArgumentException(String.format("Unsupported image type: %s", contentType));
        }
        return extension;
    }

    public static String getVideoExtension(String contentType) {
        String extension;
        if ("video/mp4".equals(contentType)) {
            extension = "mp4";
        } else if ("video/webm".equals(contentType)) {
            extension = "webm";
        } else {
            throw new IllegalArgumentException(String.format("Unsupported video type: %s", contentType));
        }
        return extension;
    }

    public static void validationImageExtension(String contentType) {
        if(!contentType.equals("image/png") && !contentType.equals("image/jpeg") && !contentType.equals("image/jpg")) {
            throw new IllegalArgumentException(String.format("Unsupported image type: %s", contentType));
        }
    }

    public static void validationVideoExtension(String contentType) {
        if(!contentType.equals("video/mp4") && !contentType.equals("video/webm")) {
            throw new IllegalArgumentException(String.format("Unsupported video type: %s", contentType));
        }
    }

    public static String getMediaType(String fileName){
        fileName = fileName.toLowerCase();
        if (fileName.endsWith(".png")){
            return "image/png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")){
            return "image/jpeg";
        } else if (fileName.endsWith(".mp4")){
            return "video/mp4";
        } else if (fileName.endsWith(".webm")){
            return "video/webm";
        } else {
            throw new IllegalArgumentException(String.format("Unsupported file type: %s",fileName));
        }
    }
}
