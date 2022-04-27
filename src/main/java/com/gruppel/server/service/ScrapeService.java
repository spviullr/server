package com.gruppel.server.service;

import com.gruppel.server.entity.Film;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
                    //TODO: klären, ob Kategorien gesplittet werden müssen oder nicht
                    final String length = row.select(".runtime").text();
                    final String date = row.select(".unbold.text-muted.lister-item-year").text();
                    String people = row.select("p a").text();
                    String[] names = people.split(" ");
                    final String regie = names[0] + " " + names[1];
                    final String writer = "";
                    //TODO: Writer nicht in IMDB Liste enthalten, workaround planen
                    final Elements banner = row.select("img[src$= .png]");
                    String cast = "";
                    int z = 2;
                    while (z < names.length) {
                        //TODO: Problem mit Doppelnamen: Bsp. "Milly Bobby Brown", werden falsch getrennt
                        cast = cast + " " + names[z];
                        z++;
                        if (z % 2 == 0) {
                            cast = cast + ",";
                        }
                    }
                    log.info("\n    Titel: " + title +
                            "\n     Regie: " + regie +
                            "\n     Genre: " + category +
                            "\n     Länge: " + length +
                            "\n     Erscheinungsjahr: " + date +
                            "\n     Cast: " + cast +
                            "\n     Banner: " + banner.attr("src"));

                    //DONE: erstelleFilm() mit den gegebenen Parametern ausführen
                    filmList.add(new Film(title, category, length, date, regie, writer, cast, banner.attr("src")));

                    System.out.println("\n link: " + banner.attr("src"));

                    anzahlFilme++;

                    if(anzahlFilme >= maxAmount){
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
        String url = "";

        if (anzahlFilme != 0) {
            url = "&start=" + siteUpdater;
        }

        if (yearMin == null || yearMax == null) {
            return "https://www.imdb.com/search/title/?title_type=feature&count=250" + (genre != null ? "&genres=" + genre : "") + url;
        }

        return "https://www.imdb.com/search/title/?title_type=feature&release_date=" + yearMin + "-01-01," + yearMax
                + "-01-01&count=250" + (genre != null ? "&genres=" + genre : "") + url;
    }

}
