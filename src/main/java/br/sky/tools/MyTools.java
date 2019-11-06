package br.sky.tools;


import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import org.controlsfx.control.CheckListView;

/**
 *
 * @author AndreLopes
 */
public class MyTools {

    private static final int DEFAULT_WIDTH = 1024;
    private static final int DEFAULT_HEIGHT = 768;




    /**
     *
     * Simple workaround: simply register a keyListener to the list and when you
     * press Space handle the items which are not handled by JavaFx:
     *
     * https://stackoverflow.com/questions/57966065/javafx-selectionmodel-of-checklistview/57969890#57969890
     *
     * @param selectedIndices
     * @param list
     */
    public static void revertCheck(ObservableList<Integer> selectedIndices, CheckListView<String> list) {
        selectedIndices.forEach(index -> {
            // If needed to skip the selected index which is handled by JavaFx
            if (!index.equals(list.getSelectionModel().getSelectedIndex())) {
                if (list.getCheckModel().isChecked(index)) {
                    list.getCheckModel().clearCheck(index);
                } else {
                    list.getCheckModel().check(index);
                }
            }
        });
    }

    public static String getTempSaveFileName(String prefix, String extension) {
        String randomWord = "";
        try {
            randomWord = MyTools.generateRandomWords(1)[0];
        } catch (Exception ex) {

            Random rn = new Random();
            randomWord = "" + rn.nextInt(10);
        }

        String saveAt = "";

        try {
            saveAt = System.getProperty("java.io.tmpdir");
        } catch (Exception ex) {

        }

        String saveAs;
        if (prefix != null) {
            saveAs = prefix + "_" + randomWord;
        } else {
            saveAs = prefix + randomWord;
        }

        saveAt = saveAt + saveAs + extension;
        return saveAt;
    }

    public static String getLocalHostName() {
        String hostName = "";
        try {
            hostName = InetAddress.getLocalHost().getHostName();

        } catch (UnknownHostException ex) {
            hostName = "";

        }
        return hostName;
    }

    public static String getLocalHostAddress() {
        String hostAddress = "";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();

        } catch (UnknownHostException ex) {
            hostAddress = "";

        }
        return hostAddress;
    }


    public static LocalDate convertSqlDateToLocalDate(java.sql.Date date) {
        if (date == null) {
            return null;
        } else {
            return date.toLocalDate();
        }
    }

    public static java.util.Date convertSqlDateToDate(java.sql.Date sqlDate) {
        java.util.Date javaDate = null;
        if (sqlDate != null) {
            javaDate = new Date(sqlDate.getTime());
        }
        return javaDate;
    }

    public static Calendar toCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static long convertDateToLong(Date dt) {
        return dt.getTime();

    }

    public static Date convertLongToDate(long longValue) {
        return new Date(longValue);
    }

    public static LocalDate convertLongToLocalDate(long longValue) {
        return Instant.ofEpochMilli(longValue).atZone(ZoneId.of("America/Sao_Paulo")).toLocalDate();
    }

    public static LocalDateTime convertLongToLocalDateTime(long longValue) {
        return Instant.ofEpochMilli(longValue).atZone(ZoneId.of("America/Sao_Paulo")).toLocalDateTime();
    }

    public static long convertLocalDateToLong(LocalDate dt) {
        return dt.atStartOfDay(ZoneOffset.systemDefault()).toInstant().toEpochMilli();

    }

    public static java.sql.Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

    public static LocalDate convertToLocalDateViaMilisecond(Date dateToConvert) {
        if (dateToConvert == null) {
            return null;
        }
        try {
            return Instant.ofEpochMilli(dateToConvert.getTime()).atZone(ZoneId.of("America/Sao_Paulo")).toLocalDate();
        } catch (Exception ex) {

        }
        return null;
    }

    public static Map<Currency, Locale> getCurrencyLocaleMap() {
        Map<Currency, Locale> map = new HashMap<>();
        for (Locale locale : Locale.getAvailableLocales()) {
            try {
                Currency currency = Currency.getInstance(locale);
                map.put(currency, locale);
            } catch (Exception e) {
                // skip strange locale
            }
        }
        return map;
    }


    public static Number roundTo2Decimals(double val) {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setDecimalSeparator('.');

        DecimalFormat df2 = new DecimalFormat("###.##", symbols);

        try {
            return df2.parse(df2.format(val)).doubleValue();
        } catch (ParseException ex) {
            Logger.getLogger(MyTools.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return val;
        }

    }

    /**
     *
     * @param i An Integer
     * @return
     */
    public static String numberToAlphabetic(int i) {

        if (i < 0) {
            return "-" + numberToAlphabetic(-i - 1);
        }

        int quot = i / 27;
        int rem = i % 27;
        char letter = (char) ((int) '@' + rem);
        if (quot == 0) {
            return "" + letter;
        } else {
            return numberToAlphabetic(quot - 1) + letter;
        }
    }

    public static String alphabeticToNumber(String str) {
        String c = "?";

        int MAX_ATTEMPTS = 100;
        int attempts = 0;
        while (attempts < MAX_ATTEMPTS) {

            String x = numberToAlphabetic(attempts);
            if (x.equalsIgnoreCase(str)) {
                c = "" + attempts;
                break;
            }
            attempts++;
        }

        return c;
    }




    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }



    public static String cleanString(String string) {
        try {
            string = Normalizer.normalize(string, Normalizer.Form.NFD);
            string = string.replaceAll("[^\\p{ASCII}]", "");
            string = string.trim();
            string = string.toLowerCase();
        } catch (Exception ex) {
            return "";
        }
        return string;
    }

    public static String[] generateRandomWords(int numberOfWords) {
        String[] randomStrings = new String[numberOfWords];
        Random random = new Random();
        for (int i = 0; i < numberOfWords; i++) {
            char[] word = new char[10]; // words of length  10.
            for (int j = 0; j < word.length; j++) {
                word[j] = (char) ('a' + random.nextInt(26));
            }
            randomStrings[i] = new String(word);
        }
        return randomStrings;
    }

    /**
     * Devolve a data atual em extenso.
     *
     * @return Devolve a data atual em extenso, utiliza DateFormat,
     * df.format(new Date());
     */
    public static String getDataPorExtenso() {
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, new Locale("pt", "BR"));
        return df.format(new Date());
    }

//    public static void setFavicon(JFrame frame) {
//        try {
//            ImageIcon favicon = MyResources.getIcon("favicon.png");
//            frame.setIconImage(favicon.getImage());
//        } catch (Exception ex) {
//
//            
//        }
//    }
    /**
     *
     * @param ts
     * @return a data no formato dd/MM/yyyy
     */
    public static String formatDate(Timestamp ts, String format) {
        try {

            if (ts == null) {
                return "";
            }

            SimpleDateFormat df = new SimpleDateFormat(format);
            return df.format(new java.util.Date(ts.getTime()));
        } catch (Exception ex) {

        }
        return "";

    }

    /**
     *
     * @param ts
     * @return a data no formato dd/MM/yyyy
     */
    public static String formatDate(Timestamp ts) {
        try {

            if (ts == null) {
                return "";
            }

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            return df.format(new java.util.Date(ts.getTime()));
        } catch (Exception ex) {

        }
        return "";

    }

    /**
     *
     * @param date
     * @return a data no formato dd/MM/yyyy
     */
    public static String formatDate(LocalDate date) {

        try {

            if (date == null) {
                return "";
            }

            String format = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            return format;
        } catch (Exception ex) {
            Logger.getLogger(MyTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String formatDate(LocalDate date, String pattern) {
        try {

            if (date == null) {
                return "";
            }

            String format = date.format(DateTimeFormatter.ofPattern(pattern));
            return format;
        } catch (Exception ex) {
            Logger.getLogger(MyTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String formatDate(Date date, String pattern) {
        try {

            if (date == null) {
                return "";
            }

            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            String format = sdf.format(date);
            return format;
        } catch (Exception ex) {
            Logger.getLogger(MyTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    /**
     *
     * @param date
     * @param sdf
     * @return a data no formato sdf
     */
    public static String formatDate(Date date, DateFormat sdf) {
        try {

            if (date == null) {
                return "";
            }

            String format = sdf.format((Date) date);
            return format;
        } catch (Exception ex) {
            Logger.getLogger(MyTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    /**
     *
     * @param date
     * @return a data no formato dd/MM/yyyy
     */
    public static String formatDate(java.util.Date date) {

        if (date == null) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return sdf.format(new java.util.Date(date.getTime()));
        } catch (Exception ex) {
            Logger.getLogger(MyTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getCurrentTimeString() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        return dateFormat.format(new Date());
    }

    public static String getMD5Hash(File file) throws IOException, NoSuchAlgorithmException {
        //Use MD5 algorithm
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");

        //Get the checksum
        String checksum = getFileChecksum(md5Digest, file);
        return checksum;
    }

    private static String getFileChecksum(MessageDigest digest, File file) throws IOException {
        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);

        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        };

        //close the stream; We don't need it now.
        fis.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        //return complete hash
        return sb.toString();
    }

    /**
     *
     * @param userInput A informacao que o usuario digitou.
     * @param compareTo
     * @return true se a string userInput esta contida no objeto "compareTo".
     */
    public static boolean contains(String userInput, Object compareTo) {
        boolean valid = false;
        //
        if (compareTo == null) {
            return false;
        }

        if (compareTo instanceof String) {

            //Strings em que serão comparadas
            String compareToString = (String) compareTo;

            if (compareToString.contains(userInput)) {
                valid = true;
            }

            if (cleanString(compareToString).contains(cleanString(userInput))) {
                valid = true;
            }

        }

        //
        return valid;
    }

    public static boolean equals(String userInput, Object compareTo) {
        boolean valid = false;
        //
        if (compareTo == null) {
            return false;
        }

        if (compareTo instanceof String) {

            //Strings em que serão comparadas
            String compareToString = (String) compareTo;

            if (compareToString.equalsIgnoreCase(userInput)) {
                valid = true;
            }

            if (cleanString(compareToString).equalsIgnoreCase(cleanString(userInput))) {
                valid = true;
            }

        }

        //
        return valid;
    }

}
