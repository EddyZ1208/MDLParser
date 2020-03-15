package com.eddyzou.mdlparser;

import com.eddyzou.mdlparser.impl.MDLParser;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.management.modelmbean.ModelMBean;

import com.eddyzou.mdlparser.dao.MDLObject;

public class test {
	
	public static void main(String[] args) {
		try {
			MDLParser parser=new MDLParser("D:\\mdltest\\ICW001.mdl","D:\\mdltest\\ICW001test.mdl");
			MDLParser applicationParser=new MDLParser("D:\\mdltest\\application.mdl");
			int result=parser.CenterController();
			if(result>0) {
				applicationParser.CenterController();
				MDLObject app=applicationParser.getApplication();
				parser.model.insertApp(app);
				parser.outputFile(parser.model);
				parser.outputDone();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}


