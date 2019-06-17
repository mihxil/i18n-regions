package org.meeuw.springboot;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ClassUtils;
import org.meeuw.i18n.Region;
import org.meeuw.i18n.Regions;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegionsController {

    @RequestMapping(value = "/")
    public void index(
        @RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) Locale language,
        HttpServletResponse response) throws Exception {
        html(response, writer -> {
            writer.println("<p>With translations to: " + language + "</p>");
            writer.println("<ul>");
            Regions.values().forEach((r) -> {
                li(writer, r, language);
            });
            writer.println("</ul>");
        });
    }

    @RequestMapping("/{region}")
    public void region(
        @PathVariable("region") String region,
        @RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) Locale language,
        HttpServletResponse response) throws Exception {
        html(response, writer -> {
            Region r = Regions.getByCode(region).orElseThrow(IllegalArgumentException::new);
            writer.println("<h1>" + r.getName() + ": " + r.getName(language) + "</h1>");
            writer.println("<p>code:" + r.getCode() + "</p>");
            writer.println("<p>class: <a href='/type/" + r.getClass().getName() + "'>" + r.getClass().getName() + "</a></p>");
            for (Class<?> i : ClassUtils.getAllInterfaces(r.getClass())) {
                writer.println("<p>interface: <a href='/type/" + i.getName() + "'>" + i.getName() + "</a></p>");
            }
        });
    }

    @RequestMapping("/type/{type}")
    public void type(
        @PathVariable("type") String type,
        @RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) Locale language,
        HttpServletResponse response) throws Exception {
        html(response, writer -> {
            Class < ? extends Region> t = (Class<Region>) Class.forName(type);
            Regions.values(t).forEach(r -> {
                li(writer, r, language);
            });
        });
    }

    protected void li(PrintWriter writer, Region r, Locale languageCode) {

        writer.println("<li>");
        writer.println("<a href='/" + r.getCode() + "'>" + r.getCode() + ":" + r.getName() + ":" + r.getName(languageCode) + "</a>");
        writer.println("</li>");
    }


    protected void html(HttpServletResponse response, ThrowingConsumer<PrintWriter> writerConsumer) throws Exception {
        response.setHeader(HttpHeaders.CONTENT_TYPE, "text/html;charset=UTF-8");
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8));
        writer.println("<html><body>");
        writerConsumer.accept(writer);
        writer.println("</body></html>");
        writer.close();
    }

    @FunctionalInterface
    public interface ThrowingConsumer<T> {
        void accept(T t) throws Exception;
    }


}
