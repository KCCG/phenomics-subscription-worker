package au.org.garvan.kccg.subscription.worker.constants;

import au.org.garvan.kccg.subscription.worker.models.EmailArticle;
import au.org.garvan.kccg.subscription.worker.models.EmailContentsMain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * Created by ahmed on 11/1/18.
 */
public class EmailHTML {

    private static String _1HEADER = "<!DOCTYPE html>\n<html>\n<body>";

    private static String _2WELCOME = "<h2 style=\"color: #5e9ca0;\"><span style=\"color: #000000;\">Phenomics Pubmed Digest</span></h2>\n";

    private static String _3COUNT = "<p style=\"color: #2e6c80;\"><span style=\"color: #000000;\">We have found</span> <span style=\"color: #800000;\">#Phenomics:TotalArticles# <span style=\"color: #000000;\">matching your search query.</span>&nbsp;</span></p>";

    private static String _4DIGESTCOUNT = "<p style=\"color: #2e6c80;\">Following are #Phenomics:DigestSize# articles along with relevant details.&nbsp;</p>\n";

    private static String _5ITEM = "<h4><span style=\"color: #800000;\">#Phenomics:ArticleNumber#. #Phenomics:ArticleTitle#</span><br /> [<a href=\"#Phenomics:ArticleURL#\"><span style=\"color: #808080;\">#Phenomics:ArticlePMID#</span></a>]<br />Genes: #Phenomics:GenesString#</h4>\n<hr style=\"border-top: dotted 1px;\" />";

    private static String _6FOOTER = "<p>&nbsp;</p>\n <p><br /><em>Your are receiving this email as you have a subscribed search with #Phenomics:SubscriptionTitle# title. Click here to unsubscribe this search.</em></p>\n</body>\n</html>\n";


    private String getHeader() {
        return _1HEADER;
    }

    private String getWelcome() {
        return _2WELCOME;

    }

    private String getCount(String totalArticles) {
        String tempCount = _3COUNT;
        tempCount.replace("#Phenomics:TotalArticles#", totalArticles);
        return tempCount;
    }

    private String getDigestCount(String digestCount) {
        String tempDisgestCount = _4DIGESTCOUNT;
        tempDisgestCount.replace("#Phenomics:DigestSize#", digestCount);
        return tempDisgestCount;
    }

    private String getItem(Integer articleNumner, String articleTitle, String pubMedID, String genes) {
        String tempItem = _5ITEM;
        String URL = "https://www.ncbi.nlm.nih.gov/pubmed/" + pubMedID;
        tempItem.replace("#Phenomics:ArticleNumber#", String.valueOf(articleNumner));
        tempItem.replace("#Phenomics:ArticleTitle#", articleTitle);
        tempItem.replace("#Phenomics:ArticlePMID#", pubMedID);
        tempItem.replace("#Phenomics:ArticleURL#", URL);
        tempItem.replace("#Phenomics:GenesString#", genes);
        return tempItem;
    }

    public StringBuilder constructEmail(EmailContentsMain emailContents) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getHeader());
        stringBuilder.append(getWelcome());
        stringBuilder.append(getCount(String.valueOf(emailContents.getTotalArticles())));

        String digestString;
        if (emailContents.getTotalArticles() == emailContents.getDigestArticles()) {
            digestString = "searched";
        } else {
            digestString = "top " + String.valueOf(emailContents.getDigestArticles());
        }
        stringBuilder.append(getDigestCount(digestString));

        for (EmailArticle emailArticle : emailContents.getLstItems()) {
            String genes = "";
            if (emailArticle.getQueriedGenes().size() > 0) {
                genes = genes + constructGeneString(emailArticle.getQueriedGenes());
            }
            if (emailArticle.getOtherGenes().size() > 0) {
                if (genes != "")
                    genes = genes + " | ";
                genes = genes + constructGeneString(emailArticle.getOtherGenes());
            }
            String item = getItem(emailArticle.getId(), emailArticle.getTitle(), emailArticle.getPMID(), genes);
            stringBuilder.append(item);
        }
        return stringBuilder;

    }

    private String constructGeneString(Map<String, Integer> mapGenes) {
        Map<String, Integer> sortedQueriedGenes = mapGenes.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(toMap(Entry::getKey, Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
        List<String> geneStrings = new ArrayList<>();
        for (Entry<String, Integer> entry : sortedQueriedGenes.entrySet()) {
            geneStrings.add(String.format("%s(%d)", entry.getKey(), entry.getValue()));
        }
        return geneStrings.stream().collect(Collectors.joining(","));
    }

}

