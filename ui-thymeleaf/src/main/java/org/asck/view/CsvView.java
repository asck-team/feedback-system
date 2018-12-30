package org.asck.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.asck.service.client.model.Answer;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

public class CsvView extends AbstractCsvView {

	
	@Override
    protected void buildCsvDocument(Map<String, Object> model, HttpServletRequest request, HttpServletResponse
            response) throws Exception {

        response.setHeader("Content-Disposition", "attachment; filename=\"my-csv-file.csv\"");
        
        @SuppressWarnings("unchecked")
        List<Answer> answers = (List<Answer>) model.get("answers");
        String[] header = {"FirstName", "LastName", "LastName", "JobTitle", "Company", "Address", "City", "Country",
                "PhoneNumber"};
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);

        csvWriter.writeHeader(header);

        for (Answer answer : answers) {
            csvWriter.write(answer, header);
        }
        csvWriter.close();

    }

}
