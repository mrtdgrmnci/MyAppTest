package com.mrt.utils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;



public class ReadData {

    String excelFile = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
            + File.separator + "resources" + File.separator + "LoginCredentials.xlsx";
    String knownIssues = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
            + File.separator + "resources" + File.separator + "knownIssues.xlsx";
    String emailData = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
            + File.separator + "resources" + File.separator + "emailData.xlsx";
    public ArrayList<String> loginDetails;
    public ArrayList<String> issues;

    public ArrayList<String> getLoginCredentials(String role) throws EncryptedDocumentException, IOException {
        loginDetails = new ArrayList<>();
        String dec_pw = EncryptionUtils.decrypt(ConfigReader.getProperty("pw"));
        Workbook workbook = WorkbookFactory.create(new File(excelFile), dec_pw);
        Sheet workSheet = workbook.getSheet("credentials");

        int rowNum = workSheet.getPhysicalNumberOfRows();
        int colNum = workSheet.getRow(0).getLastCellNum();

        Cell cell;
        row: for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                cell = workSheet.getRow(i).getCell(j);
                String userName = workSheet.getRow(i).getCell(0).toString();
                String password = workSheet.getRow(i).getCell(1).toString();
                String env = workSheet.getRow(i).getCell(3).toString();
                String cellData = cell.toString();
                if (cellData.equalsIgnoreCase(role) && env.equalsIgnoreCase(ConfigReader.getProperty("env"))) {
                    loginDetails.add(userName);
                    loginDetails.add(password);
                    break row;
                }
            }

        }
        workbook.close();
        return loginDetails;
    }

    public ArrayList<String> getAllIssues(String status) {
        issues = new ArrayList<>();
        Workbook workbook = null;
        try {
            String dec_pw = EncryptionUtils.decrypt(ConfigReader.getProperty("pw"));
            workbook = WorkbookFactory.create(new File(knownIssues), dec_pw);
        } catch (EncryptedDocumentException | IOException io) {
            io.printStackTrace();
        }

        Sheet workSheet = workbook.getSheet("browserErrors");

        int rowNum = workSheet.getPhysicalNumberOfRows();
        int colNum = workSheet.getRow(0).getLastCellNum();

        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                String error = workSheet.getRow(i).getCell(0).toString();
                String statusCol = workSheet.getRow(i).getCell(1).toString();
                if (statusCol.equals(status)) {
                    issues.add(error);
                }
            }

        }
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return issues;
    }

    public ArrayList<String> getAllIssueComments(String status) {
        issues = new ArrayList<>();
        Workbook workbook = null;
        try {
            String dec_pw = EncryptionUtils.decrypt(ConfigReader.getProperty("pw"));
            workbook = WorkbookFactory.create(new File(knownIssues), dec_pw);
        } catch (EncryptedDocumentException | IOException io) {
            io.printStackTrace();
        }

        Sheet workSheet = workbook.getSheet("browserErrors");

        int rowNum = workSheet.getPhysicalNumberOfRows();
        int colNum = workSheet.getRow(0).getLastCellNum();

        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                String comment = workSheet.getRow(i).getCell(2).toString();
                String statusCol = workSheet.getRow(i).getCell(1).toString();
                if (statusCol.equals(status)) {
                    issues.add(comment);
                }
            }

        }
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return issues;
    }

    public ArrayList<String> getEmails() {
        ArrayList<String> emails = new ArrayList<>();
        Workbook workbook = null;
        try {
            String dec_pw = EncryptionUtils.decrypt(ConfigReader.getProperty("pw"));
            workbook = WorkbookFactory.create(new File(emailData), dec_pw);
        } catch (EncryptedDocumentException | IOException io) {
            io.printStackTrace();
        }
        Sheet workSheet = workbook.getSheet("emails");
        int rowNum = workSheet.getPhysicalNumberOfRows();
        for (int i = 1; i < rowNum; i++) {
            String email = workSheet.getRow(i).getCell(0).toString();
            String approval = workSheet.getRow(i).getCell(2).toString();
            if (approval.equalsIgnoreCase("TRUE")) {
                emails.add(email);
            }
        }
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return emails;
    }
}
