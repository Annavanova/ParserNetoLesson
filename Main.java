import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

        List<Employee> list = parseCSV(columnMapping, fileName);

        String json = listToJson(list);
        System.out.println(json);

        writeString(json);
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {

        List<Employee> employeeList = new ArrayList<>();

        try (CSVReader readerCSV = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategyCPM = new ColumnPositionMappingStrategy<>();
            strategyCPM.setType(Employee.class);
            strategyCPM.setColumnMapping(columnMapping);

            CsvToBean<Employee> csvToBean = new CsvToBeanBuilder<Employee>(readerCSV).withMappingStrategy(strategyCPM).build();
            employeeList = csvToBean.parse();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return employeeList;
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);

        return json;
    }

    private static void writeString(String jsonString) {
        try (FileWriter fileWriter = new FileWriter("data.json")) {
            fileWriter.write(jsonString);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
