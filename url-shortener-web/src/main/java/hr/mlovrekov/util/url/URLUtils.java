package hr.mlovrekov.util.url;

public abstract class URLUtils {

    private static final String PROTOCOL_SEPARATOR = "://";

    /**
     * Extracts the protocol, hostname and port from the given url. Does not verify url validity.
     * @param url
     * @return
     */
    public static String extractRootFromURL(String url) {
        String root;

        if (url.contains(PROTOCOL_SEPARATOR)) {
            String[] splitUrl = url.split(PROTOCOL_SEPARATOR);
            String protocol = splitUrl[0];
            String rest = splitUrl[1];
            root = protocol + PROTOCOL_SEPARATOR + rest.split("/")[0];
        } else {
            root = url.split("/")[0];
        }

        return root;
    }
}
