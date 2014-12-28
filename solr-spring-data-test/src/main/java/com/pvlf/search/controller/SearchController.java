package com.pvlf.search.controller;

import com.pvlf.search.model.SearchCommand;
import com.pvlf.search.model.SitePageDocument;
import com.pvlf.search.service.SearchService;

import java.util.Locale;

import javax.annotation.Resource;

import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/search/*")
public class SearchController {

	@Resource
	private SearchService searchService;
	
	@RequestMapping("view.do")
	public String view(Locale locale, Model model, @ModelAttribute SearchCommand searchCommand) {

		return "search";
	}

	@RequestMapping("search.do")
	public String search(Model model, @ModelAttribute SearchCommand searchCommand, Pageable pageable) {
		
		String searchTerm = searchCommand.getSearchTerm();
		//TODO: add validator
		if (StringUtils.hasText(searchTerm)) {
			HighlightPage<SitePageDocument> page = searchService.search(searchTerm.trim(), pageable);
			model.addAttribute("page", page);
		}

		model.addAttribute("searchCommand", searchCommand);

		return "search";
	}
	
}
