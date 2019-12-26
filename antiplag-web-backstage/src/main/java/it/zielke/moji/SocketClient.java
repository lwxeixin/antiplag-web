package it.zielke.moji;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class SocketClient {
    private Socket socket;
    private Stage currentStage;
    private String server;
    private int port;
    private String userID;
    private String language;
    private int setID;
    private long optM;
    private int optD;
    private int optX;
    private long optN;
    private String optC;
    private URL resultURL;
    private List<String> supportedLanguages;
    private OutputStream out;
    private BufferedReader in;

    public SocketClient() {
        this.currentStage = Stage.DISCONNECTED;
        this.setID = 1;
        this.optM = 10L;
        this.optD = 0;
        this.optX = 0;
        this.optN = 250L;
        this.optC = "";
        this.supportedLanguages = Arrays.asList("c", "cc", "java", "ml", "pascal", "ada", "lisp", "schema", "haskell", "fortran", "ascii", "vhdl", "perl", "matlab", "python", "mips", "prolog", "spice", "vb", "csharp", "modula2", "a8086", "javascript", "plsql");
        this.server = "moss.stanford.edu";
        this.port = 7690;
        this.language = "java";
    }

    public SocketClient(String server, int port) {
        this();
        this.server = server;
        this.port = port;
    }

    public SocketClient(String server, int port, String language) {
        this(server, port);
        this.language = language;
    }

    public void close() {
        try {
            this.sendCommand("end\n");
            this.out.close();
            this.in.close();
            this.socket.close();
        } catch (MossException | IOException ignored) {
        } finally {
            this.currentStage = Stage.DISCONNECTED;
        }

    }

    public void connect() throws UnknownHostException, IOException, SecurityException {
        if (this.currentStage != Stage.DISCONNECTED) {
            throw new RuntimeException("Client is already connected");
        } else {
            this.socket = new Socket(this.server, this.port);
            this.socket.setKeepAlive(true);
            this.out = this.socket.getOutputStream();
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), Charsets.US_ASCII));
            this.currentStage = Stage.AWAITING_INITIALIZATION;
        }
    }

    public Stage getCurrentStage() {
        return this.currentStage;
    }

    /** @deprecated */
    @java.lang.SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    public int getIncSetID() {
        return this.setID++;
    }

    public String getLanguage() {
        return this.language;
    }

    public String getOptC() {
        return this.optC;
    }

    public int getOptD() {
        return this.optD;
    }

    public long getOptM() {
        return this.optM;
    }

    public long getOptN() {
        return this.optN;
    }

    public int getOptX() {
        return this.optX;
    }

    public int getPort() {
        return this.port;
    }

    public URL getResultURL() {
        return this.resultURL;
    }

    public String getServer() {
        return this.server;
    }

    public int getSetID() {
        return this.setID;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public List<String> getSupportedLanguages() {
        return this.supportedLanguages;
    }

    public String getUserID() {
        return this.userID;
    }

    public String readFromServer() throws IOException {
        return this.in.readLine();
    }

    public void run() throws MossException, IOException, UnknownHostException {
        this.connect();
        this.sendInitialization();
        this.sendLanguage();
    }

    private void sendCommand(Object... objects) throws MossException {
        Vector<String> commandStrings = new Vector<>();
        String[] commandArray = new String[commandStrings.size()];

        for (Object o : objects) {
            String s = o.toString();
            commandStrings.add(s);
        }

        this.sendCommandStrings((String[]) commandStrings.toArray(commandArray));
    }

    private void sendCommandStrings(String... strings) throws MossException {
        if (strings != null && strings.length != 0) {
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < strings.length; ++i) {
                String s = strings[i];
                sb.append(s);
                if (i != strings.length - 1) {
                    sb.append(" ");
                }
            }

            sb.append('\n');

            try {
                byte[] bytes = sb.toString().getBytes(Charsets.US_ASCII);
                this.out.write(bytes);
                this.out.flush();
            } catch (IOException var5) {
                throw new MossException("Failed to send command: " + var5.getMessage());
            }

        } else {
            throw new MossException("Failed to send command because it was empty.");
        }
    }

    public void sendInitialization() throws MossException {
        if (this.currentStage != Stage.AWAITING_INITIALIZATION) {
            throw new RuntimeException("Cannot send initialization. Client is either already initialized or not connected yet.");
        } else {
            this.sendCommand("moss", this.userID);
            this.sendCommand("directory", this.optD);
            this.sendCommand("X", this.optX);
            this.sendCommand("maxmatches", this.optM);
            this.sendCommand("show", this.optN);
            this.currentStage = Stage.AWAITING_LANGUAGE;
        }
    }

    public void sendLanguage() throws MossException, IOException {
        if (this.currentStage != Stage.AWAITING_LANGUAGE) {
            throw new RuntimeException("Language already sent or client is not initialized yet.");
        } else {
            this.sendCommand("language", this.language);
            String serverString = this.readFromServer();
            if (serverString != null && serverString.trim().toLowerCase(Locale.ENGLISH).equals("yes")) {
                this.currentStage = Stage.AWAITING_FILES;
            } else {
                throw new MossException("MOSS Server does not recognize this programming language");
            }
        }
    }

    public void sendLanguage(String language) throws MossException, IOException {
        this.setLanguage(language);
        this.sendLanguage();
    }

    public void sendQuery() throws MossException, IOException {
        if (this.currentStage != Stage.AWAITING_QUERY) {
            throw new RuntimeException("Cannot send query at this time. Connection is either not initialized or already closed");
        } else if (this.setID == 1) {
            throw new MossException("You did not upload any files yet");
        } else {
            this.sendCommand(String.format(Locale.ENGLISH, "%s %d %s", "query", 0, this.optC));
            this.currentStage = Stage.AWAITING_RESULTS;
            String result = this.readFromServer();
            if (result != null && result.toLowerCase(Locale.ENGLISH).startsWith("http")) {
                try {
                    this.resultURL = new URL(result.trim());
                } catch (MalformedURLException var3) {
                    throw new MossException("MOSS submission failed. The server did not return a valid URL with detection results.", var3);
                }

                this.currentStage = Stage.AWAITING_END;
            } else {
                throw new MossException("MOSS submission failed. The server did not return a valid URL with detection results.");
            }
        }
    }

    public void setLanguage(String language) throws MossException {
        if (!this.supportedLanguages.contains(language)) {
            throw new MossException("MOSS Server does not recognize this programming language");
        } else {
            this.language = language;
        }
    }

    public void setOptC(String optC) {
        this.optC = optC;
    }

    public void setOptM(long optM) {
        this.optM = optM;
    }

    public void setOptN(long optN) {
        this.optN = optN;
    }

    public void setOptX(int optX) {
        this.optX = optX;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void uploadFile(File file) throws IOException {
        this.uploadFile(file, false);
    }

    public void uploadBaseFile(File file) throws IOException {
        this.uploadFile(file, true);
    }

    @SuppressWarnings(
            value = {"VA_FORMAT_STRING_USES_NEWLINE"}
    )
    public void uploadFile(File file, boolean isBaseFile) throws IOException {
        if (this.currentStage != Stage.AWAITING_FILES && this.currentStage != Stage.AWAITING_QUERY) {
            throw new RuntimeException("Cannot upload file. Client is either not initialized properly or the connection is already closed");
        } else {
            byte[] fileBytes = FileUtils.readFileToByteArray(file);
            String filename = this.normalizeFilename(file.getAbsolutePath());
            String uploadString = String.format(Locale.ENGLISH, "file %d %s %d %s\n", isBaseFile ? 0 : this.getIncSetID(), this.language, fileBytes.length, filename);
            System.out.println("uploading file: " + filename);
            this.out.write(uploadString.getBytes(Charsets.US_ASCII));
            this.out.write(fileBytes);
            this.currentStage = Stage.AWAITING_QUERY;
        }
    }

    public String normalizeFilename(String filename) {
        String result = Normalizer.normalize(filename, Form.NFD);
        result = FilenameUtils.normalizeNoEndSeparator(result, true).replaceAll("[^\\p{ASCII}]", "");
        return result;
    }
}
