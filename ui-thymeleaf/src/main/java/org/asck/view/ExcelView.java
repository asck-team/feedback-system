package org.asck.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.asck.service.client.model.Answer;
import org.springframework.web.servlet.view.document.AbstractXlsView;

public class ExcelView extends AbstractXlsView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		// change the file name
        response.setHeader("Content-Disposition", "attachment; filename=\"AnswersReport.xls\"");

        @SuppressWarnings("unchecked")
        List<Answer> answers = (List<Answer>) model.get("answers");
        
     // create excel xls sheet
        Sheet sheet = workbook.createSheet("Answer Detail");
        sheet.setDefaultColumnWidth(30);

        // create style for header cells
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFillForegroundColor(HSSFColor.BLUE.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        font.setBold(true);
        font.setColor(HSSFColor.WHITE.index);
        style.setFont(font);
        
     // create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("QuestionId");
        header.getCell(0).setCellStyle(style);
        header.createCell(1).setCellValue("Remark");
        header.getCell(1).setCellStyle(style);
        header.createCell(2).setCellValue("Answered At");
        header.getCell(2).setCellStyle(style);
        
        int rowCount = 1;

        for(Answer answer : answers){
            Row userRow =  sheet.createRow(rowCount++);
            userRow.createCell(0).setCellValue(answer.getQuestionId());
            userRow.createCell(1).setCellValue(answer.getRemark());
            userRow.createCell(2).setCellValue(answer.getAnsweredAt().toString());
            }

	}

}
