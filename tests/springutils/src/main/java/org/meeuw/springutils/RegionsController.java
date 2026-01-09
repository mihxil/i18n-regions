package org.meeuw.springutils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ClassUtils;
import org.meeuw.functional.ThrowingConsumer;
import org.meeuw.i18n.regions.Region;
import org.meeuw.i18n.regions.RegionService;
import org.meeuw.i18n.regions.spi.RegionProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class RegionsController {


    @RequestMapping(value = "/")
    public void index(
        @RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) Locale language,
        HttpServletResponse response) throws Exception {
        html(response, writer -> {
            writer.println("<p>With translations to: " + language + "</p>");
            ul(writer, () -> {
                for (RegionProvider<?> c : RegionService.getInstance().getProviders()) {
                    li(writer, c.getProvidedClass(), () -> writer.print(c.getClass() + ":"));
                }
                }
            );
        });
    }

    @RequestMapping("/{region}")
    public void region(
        @PathVariable("region") String region,
        @RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) Locale language,
        HttpServletResponse response) throws Exception {
        html(response, writer -> {
            Region r = RegionService.getInstance()
                .getByCode(region)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatusCode.valueOf(404), "No region with code " + region));
            writer.println("<h1>" + r.getName() + ": " + r.getName(language) + "</h1>");
            writer.println("<p>code:" + r.getCode() + "</p>");
            r.getEmoji().ifPresent(e -> {
                writer.println("<p>emoji:" + e + "</p>");
            });
            r.getIcon().ifPresent(icon -> {
                writer.println("<p>icon:<img width='100' src='" + icon + "' /></p>");
            });
            writer.println("<p>class: <a href='/type/" + r.getClass().getName() + "'>" + r.getClass().getName() + "</a></p>");
            ul(writer, () -> {
                for (Class<?> i : ClassUtils.getAllInterfaces(r.getClass())) {
                    li(writer, i, () -> {});
                }
            });
        });
    }

    @SuppressWarnings("unchecked")
    @RequestMapping("/type/{type}")
    public void type(
        @PathVariable("type") String type,
        @RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) Locale language,
        HttpServletResponse response) throws ClassNotFoundException, IOException {
        html(response, writer -> {
            Class < ? extends Region> t = (Class<Region>) Class.forName(type);
            RegionService.getInstance().values(t).forEach(r -> {
                li(writer, r, language);
            });
        });
    }

    protected void li(PrintWriter writer, Region r, Locale languageCode) {
        writer.println("<li>");
        writer.println("<a href='/" + r.getCode() + "'>" + r.getCode() + ":" + r.getName() + ":" + r.getName(languageCode) + "</a>");
        writer.println("</li>");
    }

    protected void li(PrintWriter writer, Class<?> r, Runnable pre) {
        writer.println("<li>");
        pre.run();
        writer.println("<a href='/type/" + r.getName() + "'>" + r.getName() +
            "</a>");
        writer.println("</li>");
    }

    protected void html(HttpServletResponse response, ThrowingConsumer<PrintWriter, ClassNotFoundException> writerConsumer) throws IOException, ClassNotFoundException {
        response.setHeader(HttpHeaders.CONTENT_TYPE, "text/html;charset=UTF-8");
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8));
        writer.println("<html><body>");
        writerConsumer.acceptThrows(writer);
        writer.println("</body></html>");
        writer.close();
    }

    protected void ul(PrintWriter writer, Runnable writerConsumer)  {
        writer.println("<ul>");
        writerConsumer.run();
        writer.println("</ul>");
    }




}
