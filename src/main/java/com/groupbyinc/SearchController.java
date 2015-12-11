package com.groupbyinc;

import com.groupbyinc.api.CloudBridge;
import com.groupbyinc.api.Query;
import com.groupbyinc.api.model.MatchStrategy;
import com.groupbyinc.api.model.PartialMatchRule;
import com.groupbyinc.api.model.Results;
import com.groupbyinc.common.apache.commons.lang3.StringUtils;
import com.groupbyinc.util.UrlBeautifier;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SearchController implements InitializingBean {


    private CloudBridge cloudBridge;
    private Integer defaultPageSize;
    private String defaultArea;
    private String defaultFields;
    private String defaultCollection;

    @RequestMapping("/search")
    protected Results handleSearch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        UrlBeautifier defaultUrlBeautifier = UrlBeautifier.getUrlBeautifiers().get("default");
        if (defaultUrlBeautifier == null) {
            UrlBeautifier.createUrlBeautifier("default");
            defaultUrlBeautifier = UrlBeautifier.getUrlBeautifiers().get("default");
        }

        Query query = defaultUrlBeautifier.fromUrl(request.getRequestURI(), new Query());

        String fields = ServletRequestUtils.getStringParameter(request, "f", defaultFields);
        if (StringUtils.isNotBlank(fields)) {
            query.addFields(fields.split(","));
        }

        String collection = ServletRequestUtils.getStringParameter(request, "c", defaultCollection);
        if (StringUtils.isNotBlank(collection)) {
            query.setCollection(collection);
        }

        String refinements = ServletRequestUtils.getStringParameter(request, "r", "");
        if (StringUtils.isNotBlank(refinements)) {
            query.addRefinementsByString(refinements);
        }

        String queryString = ServletRequestUtils.getStringParameter(request, "q", "");
        if (StringUtils.isNotBlank(queryString)) {
            query.setQuery(queryString);
        }

        String area = ServletRequestUtils.getStringParameter(request, "a", defaultArea);
        if (StringUtils.isNotBlank(area)) {
            query.setArea(area);
        }

        query.setSkip(ServletRequestUtils.getIntParameter(request, "p", 0));
        query.setPageSize(ServletRequestUtils.getIntParameter(request, "ps", defaultPageSize));

        MatchStrategy matchStrategy = new MatchStrategy();
        List<PartialMatchRule> rules = new ArrayList<>();
        rules.add(new PartialMatchRule().setTerms(2).setMustMatch(2));
        rules.add(new PartialMatchRule().setTerms(3).setMustMatch(3));
        rules.add(new PartialMatchRule().setTerms(4).setMustMatch(4));
        rules.add(new PartialMatchRule().setTerms(5).setMustMatch(5));
        matchStrategy.setRules(rules);
        query.setMatchStrategy(matchStrategy);

        try {
            return cloudBridge.search(query);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            return new Results().setErrors(e.getMessage());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String customerId = System.getProperty("customerId");
        String clientKey = System.getProperty("clientKey");
        if (customerId == null) {
            throw new Exception("You must provide a customerId as a system property. (In maven use mvn -DcustomerId=customer)");

        }
        if (clientKey == null) {
            throw new Exception("You must provide a clientKey as a system property. (In maven use mvn -DclientKey=ABC)");
        }

        defaultPageSize = new Integer(System.getProperty("pageSize", "10"));
        defaultArea = System.getProperty("area", null);
        defaultFields = System.getProperty("fields", "title");
        defaultCollection = System.getProperty("collection", null);
        cloudBridge = new CloudBridge(clientKey, customerId);

    }

}
