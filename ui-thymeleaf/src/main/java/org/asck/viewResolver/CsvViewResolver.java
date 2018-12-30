package org.asck.viewResolver;

import java.util.Locale;

import org.asck.view.CsvView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

public class CsvViewResolver implements ViewResolver {

	@Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        return new CsvView();
    }

}
