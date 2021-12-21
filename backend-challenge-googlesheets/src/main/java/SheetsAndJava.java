import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SheetsAndJava {
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);

    static List<List<Object>> values = new ArrayList<>();

    public static void main(String[] args) throws GeneralSecurityException, IOException {
        fetchSheet();

        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            printConsumers();
            printConsumersThatHaveChildrenAtHome();
            printConsumersThatHaveTeenagersAtHome();
        }
    }

    private static void printConsumers() {
        System.out.println("\n\n---------- consumers born between 1957 and 1967 who have a master's degree and who are married  ------------\n");
        List<List<Object>> consumers = filterConsumersBetweenYears(values, 1957, 1967);
        consumers = filterConsumersWithMasterDegree(consumers);
        consumers = filterConsumersWhoAreMarried(consumers);

        printValues(consumers);
        printPercentageOfConsumersInRelationToTotal((double) consumers.size());
    }

    public static void printPercentageOfConsumersInRelationToTotal(Double partialConsumers) {
        System.out.println("\n---------- consumers percentage in relation to total ------------\n");
        Double percentage = calculatePercentage((double) values.size(), partialConsumers);
        System.out.printf("%.2f%%", percentage);
    }

    public static void printConsumersThatHaveChildrenAtHome() {
        System.out.println("\n\n---------- Consumers that have children at home  ------------\n");
        List<List<Object>> consumers = filterConsumersByChildrenAtHome(values);

        printValues(consumers);
    }

    public static void printConsumersThatHaveTeenagersAtHome() {
        System.out.println("\n\n---------- Consumers that have teenagers at home  ------------\n");
        List<List<Object>> consumers = filterConsumersByTeenagersAtHome(values);

        printValues(consumers);
    }


    private static Credential authorize(final NetHttpTransport HTTP_TRANSPORT) throws IOException, GeneralSecurityException {
        InputStream in = SheetsAndJava.class.getResourceAsStream("/credentials.json");
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + "/credentials.json");
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static void fetchSheet() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String range = "marketing_campaign!A2:G";

        String APPLICATION_NAME = "Backend Challenge googlesheets";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, authorize(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        String SPREADSHEET_ID = "1lH9GUATi2iVBUqeF5fafG9k3x2Q_z3XzKcyZ9y1EoV8";
        ValueRange response = service.spreadsheets().values()
                .get(SPREADSHEET_ID, range)
                .execute();
        values = response.getValues();
    }

    private static List<List<Object>> filterConsumersBetweenYears(List<List<Object>> values, Integer year1, Integer year2) {
        List<List<Object>> filtered = new ArrayList<>();

        for (List row : values) {
            if (Double.parseDouble((String) row.get(1)) >= year1 && Double.parseDouble((String) row.get(1)) <= year2) {
                filtered.add(row);
            }
        }

        return filtered;
    }

    private static List<List<Object>> filterConsumersWithMasterDegree(List<List<Object>> values) {
        List<List<Object>> filtered = new ArrayList<>();

        for (List row : values) {
            if (row.get(2).equals("Master")) {
                filtered.add(row);
            }
        }

        return filtered;
    }

    private static List<List<Object>> filterConsumersWhoAreMarried(List<List<Object>> values) {
        List<List<Object>> filtered = new ArrayList<>();

        for (List row : values) {
            if (row.get(3).equals("Married")) {
                filtered.add(row);
            }
        }

        return filtered;
    }

    private static void printValues(List<List<Object>> values) {
        System.out.println("ID | Year_Birth | Education | Marital_Status | Income | Kidhome | Teenhome");
        for (List row : values) {
            System.out.printf("%s | %s | %s | %s | %s | %s | %s  \n",
                    row.get(0), row.get(1), row.get(2), row.get(3), row.get(4), row.get(5), row.get(6));
        }
    }

    private static Double calculatePercentage(Double totalValues, Double parcialValues) {
        return (parcialValues / totalValues) * 100;
    }

    private static List<List<Object>> filterConsumersByChildrenAtHome(List<List<Object>> values) {
        List<List<Object>> filtered = new ArrayList<>();

        for (List row : values) {
            if (Integer.parseInt((String) row.get(5)) == 1) {
                filtered.add(row);
            }
        }

        return filtered;
    }

    private static List<List<Object>> filterConsumersByTeenagersAtHome(List<List<Object>> values) {
        List<List<Object>> filtered = new ArrayList<>();

        for (List row : values) {
            if (Integer.parseInt((String) row.get(6)) == 1) {
                filtered.add(row);
            }
        }

        return filtered;
    }

}
