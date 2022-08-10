package com.mrt.utils;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import io.cucumber.core.backend.TestCaseState;
import io.cucumber.java.Scenario;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.TestCase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
public class CreateReport {
    static String excelFile = System.getProperty("user.dir") + File.separator + "target" + File.separator
            + ConfigReader.getProperty("currentPI") + "-TestCaseReport.xlsx";
    static String jsonFile = System.getProperty("user.dir") + File.separator + "target" + File.separator
            + "cucumber-reports" + File.separator + "Cucumber.json";
    static Workbook workbook = new XSSFWorkbook();
    static Sheet sheet = workbook.createSheet("TestCases");
    static FileOutputStream fileOut;
    static Font font = workbook.createFont();
    static CellStyle cellStyle = workbook.createCellStyle();
    static String columnNames[] = { "Story ID/Feature", "Scenario", "Test Steps", "Status", "Comment" };
    static Map<String, String> map = new HashMap<String, String>();

    // prepare excel file with headers
    public static void prepareFile() {
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 16);
        headerFont.setColor(IndexedColors.BLACK.index);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setBorderBottom(BorderStyle.MEDIUM);
        headerStyle.setBorderLeft(BorderStyle.MEDIUM);
        headerStyle.setBorderRight(BorderStyle.MEDIUM);
        headerStyle.setBorderTop(BorderStyle.MEDIUM);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columnNames.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 15000);
        }
        System.out.println("Excel file is ready!");
    }

    // writes info into excel file
    public static void writeFileInfo(Scenario scenario) {
        Collection<String> tags = scenario.getSourceTagNames();
        if (tags.contains("@" + ConfigReader.getProperty("currentPI"))) {
            font.setFontHeightInPoints((short) 12);
            font.setColor(IndexedColors.BLACK.index);
            cellStyle.setFont(font);
            cellStyle.setBorderBottom(BorderStyle.MEDIUM);
            cellStyle.setBorderLeft(BorderStyle.MEDIUM);
            cellStyle.setBorderRight(BorderStyle.MEDIUM);
            cellStyle.setBorderTop(BorderStyle.MEDIUM);
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
            cellStyle.setWrapText(true);

            int rowNum = sheet.getLastRowNum();
            Row row = sheet.createRow(rowNum + 1);

            for (int i = 0; i < columnNames.length; i++) {
                Cell cell = row.createCell(i);
                if (i == 0) {
                    String scenarioId = scenario.getUri().toString();
                    String feature = "";
                    String[] featuresPath = scenarioId.split("/");
                    feature = (featuresPath[featuresPath.length - 1].split("\\.")[0]);
                    cell.setCellValue(feature);
                } else if (i == 1) {
                    cell.setCellValue(scenario.getName());
                } else if (i == 2) {
                    try {
                        Field f = scenario.getClass().getDeclaredField("delegate");
                        f.setAccessible(true);
                        TestCaseState sc = (TestCaseState) f.get(scenario);
                        Field f1 = sc.getClass().getDeclaredField("testCase");
                        f1.setAccessible(true);
                        TestCase testCase = (TestCase) f1.get(sc);
                        List<PickleStepTestStep> testSteps = testCase.getTestSteps().stream()
                                .filter(x -> x instanceof PickleStepTestStep).map(x -> (PickleStepTestStep) x)
                                .collect(Collectors.toList());
                        String step = "";
                        int index = 1;
                        for (PickleStepTestStep ts : testSteps) {
                            step += ("Step " + index + ": "
                                    + (ts.getStep().getKeyword() + ts.getStep().getText() + "\r\n"));
                            index++;
                        }
                        step.trim();
                        cell.setCellValue(step);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                } else if (i == 3) {
                    String status = scenario.getStatus().toString();

                    if (status.equalsIgnoreCase("PASSED")) {
                        cell.setCellValue(status);
                    } else {
                        cell.setCellValue(status);
                    }
                }
                cell.setCellStyle(cellStyle);
            }
            try {
                fileOut = new FileOutputStream(excelFile);
                workbook.write(fileOut);
                fileOut.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    // merges rows
    public static void mergeRows() {
        int first = 1;
        int last = 1;
        int i = 1;
        while (i < sheet.getPhysicalNumberOfRows()) {
            first = i;
            last = i;
            for (int j = i + 1; j < sheet.getPhysicalNumberOfRows(); j++) {
                if (sheet.getRow(i).getCell(0).toString().equals(sheet.getRow(j).getCell(0).toString())) {
                    last = j;
                }
            }
            if (first < last) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(first, last, 0, 0);
                sheet.addMergedRegion(cellRangeAddress);
                i = last + 1;
            } else {
                i++;
            }
        }
        try {
            FileOutputStream fileOut = new FileOutputStream(excelFile);
            workbook.write(fileOut);
            fileOut.close();
        } catch (Throwable e) {
            System.out.println("IO expection");
        }
    }

    // adds failed steps into Map
    public static void analyzeFailedStep() {
        try {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(jsonFile));
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                JSONArray elementsArray = (JSONArray) jsonObject.get("elements");
                for (Object elements : elementsArray) {
                    JSONObject elementObject = (JSONObject) elements;
                    String scenario = elementObject.get("name").toString();
                    JSONArray stepsArray = (JSONArray) elementObject.get("steps");
                    for (Object step : stepsArray) {
                        JSONObject stepObject = (JSONObject) step;
                        String isFailedstep = stepObject.get("name").toString();
                        JSONObject result = (JSONObject) stepObject.get("result");
                        String status = (String) result.get("status");
                        if (status.equalsIgnoreCase("failed")) {
                            map.put(scenario, isFailedstep);
                        }
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    // updates excel file with comments
    public static void addCommentIntoReport() {
        analyzeFailedStep();
        int rowNum = sheet.getPhysicalNumberOfRows();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String expectedScenario = entry.getKey();
            for (int i = 1; i < rowNum; i++) {
                String actualScenario = sheet.getRow(i).getCell(1).toString();
                String actualSteps = sheet.getRow(i).getCell(2).toString();
                if (actualScenario.equals(expectedScenario) && actualSteps.contains(entry.getValue())) {
                    Row row = sheet.getRow(i);
                    Cell cell = row.createCell(4);
                    cell.setCellValue("FAILED STEP --> " + entry.getValue());
                    cell.setCellStyle(cellStyle);
                }
            }
        }
        try {
            FileOutputStream fileOut = new FileOutputStream(excelFile);
            workbook.write(fileOut);
        } catch (Throwable e) {
            System.out.println("IO expection");
        }
        for (int i = 1; i < rowNum; i++) {
            boolean isFailed = sheet.getRow(i).getCell(3).toString().equalsIgnoreCase("FAILED");
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(4);
            boolean isEmpty = false;
            if (cell == null || cell.getCellType() == CellType.BLANK) {
                isEmpty = true;
            }
            if (isFailed && isEmpty) {
                Cell cellNew = row.createCell(4);
                cellNew.setCellValue("Hooks Error");
                cellNew.setCellStyle(cellStyle);
            }
        }
        try {
            FileOutputStream fileOut = new FileOutputStream(excelFile);
            workbook.write(fileOut);
            fileOut.close();
        } catch (IOException e) {
            System.out.println("IO expection");
        }
    }

    // finalize the excel file
    public static void finalizeFile() {
        addCommentIntoReport();
        mergeRows();
        try {
            workbook.close();
        } catch (Throwable e) {
            System.out.println("IO expection");
        }
    }
}
