package org.example;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;
import java.io.FileOutputStream;
import java.io.IOException;

public class Excel {

    XSSFWorkbook workbook;
    XSSFSheet sheet;
    XSSFRow row;
    FileOutputStream out;
    Cell cell;

    public void create() throws IOException {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Comparison");
        createHeadings(0, "first");
        createHeadings(16, "second");
        out = new FileOutputStream("Comparison of searches.xlsx");
        workbook.write(out);
        out.close();
    }

    private void createHeadings(int firstRow, String generationWay){
        sheet.addMergedRegion(new CellRangeAddress(firstRow, firstRow, 0, 3));
        row = sheet.createRow(firstRow);
        cell = row.createCell(0);
        cell.setCellValue("The  " + generationWay + " type of generation");
        row = sheet.createRow(firstRow+1);
        createCellsHeadings();
    }

    private void createCellsHeadings(){
        cell = row.createCell(0);
        cell.setCellValue("Matrix size");
        cell = row.createCell(1);
        cell.setCellValue("Binary");
        cell = row.createCell(2);
        cell.setCellValue("Ladder");
        cell = row.createCell(3);
        cell.setCellValue("Exponential");
    }

    public void createRow(long binaryAverage, long ladderAverage, long expAverage, int index, int rows) throws IOException {
        out = new FileOutputStream("Comparison of searches.xlsx");
        row = sheet.createRow(index);
        cell = row.createCell(0);
        cell.setCellValue(rows + "X8192");
        cell = row.createCell(1);
        cell.setCellValue(binaryAverage);
        cell = row.createCell(2);
        cell.setCellValue(ladderAverage);
        cell = row.createCell(3);
        cell.setCellValue(expAverage);
        workbook.write(out);
        out.close();
    }

    public void createGraph(String title, int anchorColumn1, int anchorRow1, int anchorColumn2, int anchorRow2, int dataFirstRow, int dataLastRow, int dataFirstColumn, Boolean logOrNot) throws IOException {
        out = new FileOutputStream("Comparison of searches.xlsx");
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, anchorColumn1, anchorRow1, anchorColumn2, anchorRow2);
        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleText(title);
        chart.setTitleOverlay(false);
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP_RIGHT);
        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        bottomAxis.setTitle("Column");
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        leftAxis.setTitle("Binary & ladder & Exponential");
        if(logOrNot) {
            leftAxis.setLogBase(2.0);
        }
        XDDFDataSource<String> columns = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(dataFirstRow, dataLastRow, dataFirstColumn, dataFirstColumn));
        XDDFNumericalDataSource<Double> binary = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(dataFirstRow, dataLastRow, dataFirstColumn + 1, dataFirstColumn + 1));
        XDDFNumericalDataSource<Double> ladder = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(dataFirstRow, dataLastRow, dataFirstColumn + 2, dataFirstColumn + 2));
        XDDFNumericalDataSource<Double> exponential = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(dataFirstRow, dataLastRow, dataFirstColumn + 3, dataFirstColumn + 3));
        XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
        XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) data.addSeries(columns, binary);
        series1.setTitle("Binary", null);
        series1.setSmooth(true);
        series1.setMarkerStyle(MarkerStyle.STAR);
        XDDFLineChartData.Series series2 = (XDDFLineChartData.Series) data.addSeries(columns, ladder);
        series2.setTitle("Ladder", null);
        series2.setSmooth(true);
        series2.setMarkerStyle(MarkerStyle.SQUARE);
        XDDFLineChartData.Series series3 = (XDDFLineChartData.Series) data.addSeries(columns, exponential);
        series3.setTitle("Exponential", null);
        series3.setSmooth(true);
        series3.setMarkerStyle(MarkerStyle.CIRCLE);
        chart.plot(data);
        workbook.write(out);
    }

    public void createRatio() throws IOException {
        out = new FileOutputStream("Comparison of searches.xlsx");
        sheet.addMergedRegion(new CellRangeAddress(32, 32, 0, 3));
        row = sheet.createRow(32);
        cell = row.createCell(0);
        cell.setCellValue("The ratio of two generation types");
        row = sheet.createRow(33);
        createCellsHeadings();
        for(int i = 0; i < 13; ++i){
            row = sheet.createRow(i + 34);
            cell = row.createCell(0);
            XSSFRow row1 = sheet.getRow(i + 2);
            XSSFRow row2 = sheet.getRow(i + 18);
            XSSFCell cell1 = row1.getCell(0);
            cell.setCellValue(cell1.getStringCellValue());
            for(int j = 1; j < 4; ++j) {
                cell1 = row1.getCell(j);
                XSSFCell cell2 = row2.getCell(j);
                cell = row.createCell(j);
                cell.setCellValue(cell1.getNumericCellValue() / cell2.getNumericCellValue());
            }
        }
        workbook.write(out);
        out.close();
    }
}
