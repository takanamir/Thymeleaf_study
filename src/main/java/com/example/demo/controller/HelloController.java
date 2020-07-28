package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.bean.MyDataBean;
import com.example.demo.entity.MyData;
import com.example.demo.repository.MyDataRepository;
import com.example.demo.service.MyDataService;

@Controller
public class HelloController {
	@Autowired
	MyDataRepository repository;

	@Autowired
	private MyDataService service;

	@Autowired
	MyDataBean myDataBean;

	@PostConstruct
	public void init() {
		MyData d1 = new MyData();
		d1.setName("山田太郎");
		d1.setAge(52);
		d1.setPhoneNumber("090-999-999");
		d1.setMail("taro@yamada.com");
		d1.setMemo("this is my data!");
		repository.saveAndFlush(d1);
		MyData d2 = new MyData();
		d2.setName("鈴木花子");
		d2.setAge(15);
		d2.setPhoneNumber("080-888-888");
		d2.setMail("hanako@flower");
		d2.setMemo("my girl friend.");
		repository.saveAndFlush(d2);
		MyData d3 = new MyData();
		d3.setName("田中幸子");
		d3.setAge(37);
		d3.setPhoneNumber("070-777-777");
		d3.setMail("sachico@happy");
		d3.setMemo("my work friend...");
		repository.saveAndFlush(d3);
	}
	
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ModelAndView index(ModelAndView mav, Pageable pageable) {
		mav.setViewName("index");
		mav.addObject("title","Find Page");
		mav.addObject("msg","MyDataのサンプルです。");
		Page<MyData> list = repository.findAll(pageable);
		mav.addObject("datalist", list);
		return mav;
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home(@ModelAttribute("formModel") MyData mydata, ModelAndView mav) {
		mav.setViewName("index2");
		mav.addObject("msg", "This is sample content.");
		mav.addObject("formModel", mydata);
		Iterable<MyData> list = repository.findAll();
		mav.addObject("datalist", list);
		return mav;
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@Transactional(readOnly = false)
	public ModelAndView form(@ModelAttribute("formModel") @Validated MyData mydata, BindingResult result,
			ModelAndView mov) {
		ModelAndView res = null;
		if (!result.hasErrors()) {
			repository.saveAndFlush(mydata);
			res = new ModelAndView("redirect:/");
		} else {
			mov.setViewName("index2");
			mov.addObject("msg", "sorry, error is occured...");
			Iterable<MyData> list = repository.findAll();
			mov.addObject("datalist", list);
			res = mov;
		}
		return res;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView indexById(@PathVariable long id, ModelAndView mav) {
		mav.setViewName("pickup");
		mav.addObject("title", "Pickup Page");
		String table = "<table>" + myDataBean.getTableTagById(id) + "</table>";
		mav.addObject("msg", "pickup data id = " + id);
		mav.addObject("data", table);
		return mav;
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public ModelAndView edit(@ModelAttribute MyData mydata, @PathVariable int id, ModelAndView mav) {
		mav.setViewName("edit");
		mav.addObject("title", "edit mydata.");
		Optional<MyData> data = repository.findById((long) id);
		mav.addObject("formModel", data.get());
		return mav;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@Transactional(readOnly = false)
	public ModelAndView update(@ModelAttribute MyData mydata, ModelAndView mav) {
		repository.saveAndFlush(mydata);
		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable int id, ModelAndView mav) {
		mav.setViewName("delete");
		mav.addObject("title", "delete mydata.");
		Optional<MyData> data = repository.findById((long) id);
		mav.addObject("formModel", data.get());
		return mav;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@Transactional(readOnly = false)
	public ModelAndView remove(@RequestParam long id, ModelAndView mav) {
		repository.deleteById(id);
		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value = "/find", method = RequestMethod.GET)
	public ModelAndView find(ModelAndView mav) {
		mav.setViewName("find");
		mav.addObject("title", "Find Page");
		mav.addObject("msg", "MyDataのサンプルです。");
		mav.addObject("value", "");
		List<MyData> list = service.getAll();
		mav.addObject("datalist", list);
		return mav;
	}

	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public ModelAndView search(HttpServletRequest request, ModelAndView mav) {
		mav.setViewName("find");
		String param = request.getParameter("fstr");
		if (param == "") {
			mav = new ModelAndView("redirect:/find");
		} else {
			mav.addObject("title", "Find result");
			mav.addObject("msg", "「" + param + "」の検索結果");
			mav.addObject("value", param);
			List<MyData> list = service.find(param);
			mav.addObject("datalist", list);
		}
		return mav;
	}
}