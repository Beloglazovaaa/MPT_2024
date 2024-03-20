/* Объектно-ориентированное программирование. Необходимо создать класс ArrayPI
(модификатор доступа - public), в котором необходимо создать два двухмерных массива
(ввод с клавиатуры, 7 столбцов и 7 строк). Далее необходимо создать класс-наследник Matrix
(модификатор доступа - public final), в котором будут наследоваться данные матрицы и перемножаться.
Необходимо перемножить две данных матрицы и итоговую матрицу (произведение двух матриц) вывести на экран.

Реализовать программу с интерактивным консольным меню:
1. Вывести все таблицы из базы данных MySQL.
2. Создать таблицу в базе данных MySQL.
3. Ввести две матрицы с клавиатуры и каждую из них сохранить в MySQL с последующим выводом в
консоль.
4. Перемножить матрицу, сохранить перемноженную матрицу в MySQL и вывести в консоль.
5. Сохранить результаты из MySQL в Excel и вывести их в консоль. */

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class hard_6 {
}
