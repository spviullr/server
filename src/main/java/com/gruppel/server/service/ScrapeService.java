package com.gruppel.server.service;

import com.gruppel.server.entity.Film;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

@Service
public class ScrapeService {
    public List<Film> scrapeIMDB(String yearMin, String yearMax, String genre, int maxAmount) {
        List<Film> filmList = new ArrayList<>();

        try {
            int anzahlFilme = 0, siteUpdater = 251;

            String url = setUrl(yearMin, yearMax, genre, siteUpdater, anzahlFilme);
            Document document = Jsoup.connect(url).get();
            Logger log = Logger.getLogger("FilmListe");

            Calendar cal = Calendar.getInstance();

            int day = cal.get(Calendar.DATE), month = cal.get(Calendar.MONTH),
                    year = cal.get(Calendar.YEAR);

            SimpleFormatter formatter = new SimpleFormatter();
            FileHandler fileHandler = new FileHandler("FilmListe" + day + month + year + ".log");
            log.addHandler(fileHandler);
            fileHandler.setFormatter(formatter);
            log.setUseParentHandlers(false);

            log.info("\n\n   Filmliste:\n   Zeitraum: " + yearMin + "-" + yearMax +
                    "\n   Genre: " + genre + "\n");

            while (anzahlFilme < maxAmount) {

                for (Element row : document.select("div.mode-advanced.lister-item")) {

                    final String title = row.select(".lister-item-header a").text();
                    final String category = row.select(".genre").text();
                    final String length = row.select(".runtime").text();
                    final String date = row.select(".unbold.text-muted.lister-item-year").text();

                    final String banner = row.select("[src$= .png]").toString().split("loadlate=\"")[1].split("\"")[0];

                    String cast;
                    String regie;

                    String drehbuch = row.select("p a:nth-child(7)").text();
                    if (drehbuch.equals("")) {
                        regie = row.select("p a:nth-child(1)").text();
                        cast = row.select("p a:nth-child(3)").text()
                                + ", " + row.select("p a:nth-child(4)").text()
                                + ", " + row.select("p a:nth-child(5)").text()
                                + ", " + row.select("p a:nth-child(6)").text();
                    } else {
                        regie = row.select("p a:nth-child(1)").text() + " " + row.select("p a:nth-child(2)").text();
                        cast = row.select("p a:nth-child(4)").text()
                                + ", " + row.select("p a:nth-child(5)").text()
                                + ", " + row.select("p a:nth-child(6)").text()
                                + ", " + row.select("p a:nth-child(7)").text();
                    }

                    String writers = "";
                    String titleRef = row.select(".lister-item-header a[href]").attr("href").split("/title/")[1].split("/")[0];
                    String writerUrl = "https://www.imdb.com/title/" + titleRef + "/fullcredits";
                    Document fullcredits = Jsoup.connect(writerUrl).get();

                    if(fullcredits.body().getElementById("fullcredits_content").toString().contains("Writing Credits")){
                        String[] tableLines = fullcredits.body().getElementById("fullcredits_content").toString().split("Writing Credits")[1]
                                .split("</table>")[0].split("<td class=\"name\">");
                        int lineCount = 0;
                        for (String line : tableLines) {
                            if (line.contains("<a href=\"/name/")) {
                                lineCount++;
                                writers += line.split("<a href=\"/name/")[1].split("\">")[1].split("</a>")[0].trim();

                                //nach jedem autor komma setzen außer dem letzten
                                if (lineCount != tableLines.length) {
                                    writers += ", ";
                                }
                            }
                        }
                    }else{
                        System.out.println("Drehbuchautor für " + title + " nicht verfügbar!");
                    }

                    log.info("\n    Titel: " + title +
                            "\n     Regie: " + regie +
                            "\n     Genre: " + category +
                            "\n     Länge: " + length +
                            "\n     Erscheinungsjahr: " + date +
                            "\n     Cast: " + cast +
                            "\n     Drehbuchautoren: " + writers +
                            "\n     Banner: " + banner);

                    Film newFilm = new Film(title, category, length, date, regie, writers, cast, banner);
                    filmList.add(newFilm);
                    //System.out.println(newFilm.toString());

                    anzahlFilme++;

                    if (anzahlFilme >= maxAmount) {
                        break;
                    }

                    if (anzahlFilme == siteUpdater) {
                        siteUpdater = siteUpdater + 250;
                        url = this.setUrl(yearMin, yearMax, genre, siteUpdater, anzahlFilme);
                        document = Jsoup.connect(url).get();
                    }
                }
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }

        return filmList;
    }

    private String setUrl(String yearMin, String yearMax, String genre, int siteUpdater, int anzahlFilme) {
        String url;
        if (yearMin == null || yearMax == null) {
            url = "https://www.imdb.com/search/title/?title_type=feature" + (genre != null ? "&genres=" + genre : "") + "&count=250";
        } else {
            url = "https://www.imdb.com/search/title/?title_type=feature&release_date=" + yearMin + "-01-01," + yearMax + "-01-01" + (genre != null ? "&genres=" + genre : "") + "&count=250";
        }
        if (anzahlFilme == 0) {
            return url;
        } else {
            url = url + "&start=" + siteUpdater;
        }
        return url;
    }


}
