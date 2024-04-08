package hard_8;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SaveToExcelMenuItem {
    private final Connection connection;

    public SaveToExcelMenuItem(Connection connection) {
        this.connection = connection;
    }

    public void execute() {
        try {
            // Выполняем запрос для выборки всех данных из таблиц Worker и Student
            String query = "SELECT * FROM Worker";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet workerResultSet = preparedStatement.executeQuery();

            // Создаем новую книгу Excel
            XSSFWorkbook workbook = new XSSFWorkbook();
            // Создаем новый лист Excel
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Работники");

            // Записываем данные о работниках в файл Excel
            writeResultSetToExcel(workerResultSet, sheet);

            // Выполняем запрос для выборки всех данных из таблицы Student
            query = "SELECT * FROM Student";
            preparedStatement = connection.prepareStatement(query);
            ResultSet studentResultSet = preparedStatement.executeQuery();

            // Создаем новый лист Excel
            sheet = workbook.createSheet("Студенты");

            // Записываем данные о студентах в файл Excel
            writeResultSetToExcel(studentResultSet, sheet);

            // Сохраняем книгу Excel в файл
            FileOutputStream fileOut = new FileOutputStream("results.xlsx");
            workbook.write(fileOut);
            fileOut.close();

            // Выводим сообщение о завершении сохранения
            System.out.println("Результаты успешно сохранены в файл Excel.");

            // Закрываем книгу Excel
            workbook.close();
        } catch (SQLException | IOException e) {
            System.out.println("Ошибка при сохранении результатов в Excel: " + e.getMessage());
        }
    }

    private void writeResultSetToExcel(ResultSet resultSet, org.apache.poi.ss.usermodel.Sheet sheet) throws SQLException {
        // Получаем метаданные результатов запроса
        java.sql.ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Создаем заголовок для листа Excel
        Row headerRow = sheet.createRow(0);
        for (int i = 1; i <= columnCount; i++) {
            Cell cell = headerRow.createCell(i - 1);
            cell.setCellValue(metaData.getColumnName(i));
        }

        // Записываем данные из результатов запроса в файл Excel
        int rowNum = 1;
        while (resultSet.next()) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 1; i <= columnCount; i++) {
                Cell cell = row.createCell(i - 1);
                cell.setCellValue(resultSet.getString(i));
            }
        }
    }
}
