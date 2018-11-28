package com.ibm.input.handler;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bean.LineItemDTO;
import com.ibm.bean.RequisitionDTO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TransformInput {

	private static final String END_DATE = "endDate";

	private static final String START_DATE = "startDate";

	private static final String VALUE_ORDER = "valueOrder";

	private static final String LINEITEM_SOURCE_CODE = "lineitemSourceCode";

	private static final String LINEITEM_BY_PASS_FLAG = "lineitemByPassFlag";

	private static final String LINEITEM_CONTRACT_NO = "lineitemContractNo";

	private static final String LINEITEM_SUPPLIER_ID = "lineitemSupplierId";

	private static final String ORIGINATING_SYSTEM_LINE_ITEM_NUMBER = "originatingSystemLineItemNumber";

	private static final String LINEITEM_NEED_BY_DATE = "lineitemNeedByDate";

	private static final String LINE_ITEM_SEQ_NO = "lineItemSeqNo";

	private static final String LINE_ITEM_QTY = "lineItemQty";

	private static final String SPLIT_PERCENTAGE = "splitPercentage";

	private static final String LINE_ITEM_UOM = "lineItemUom";

	private static final String SUPPLIERPART_NUMBER = "supplierpartNumber";

	private static final String LINE_ITEM_CURR_CD = "lineItemCurrCd";

	private static final String LINE_ITEM_AMOUNT = "lineItemAmount";

	private static final String LINEITEM_DESCRIPTION = "lineitemDescription";

	private static final String UNSPSC_CD = "unspsc_cd";

	private static final String LINE_ITEMS = "LineItems";

	private static final String REQUESTER_WEB_ID = "RequesterWebId";

	private static final String PREPARER_WEB_ID = "PreparerWebId";

	private static final String COMMENT_TO_SUPPLIER = "CommentToSupplier";

	private static final String REQUESTER_NAME = "RequesterName";

	private static final String COMPANY_CODE = "CompanyCode";

	private static final String REQUISITION_NAME = "RequisitionName";

	private static final String UNIQUE_TRANSACTION_NUMBER = "UniqueTransactionNumber";
	
	private static final String ON_TYPE = "ORDERNOW";

	Logger logger = LoggerFactory.getLogger(TransformInput.class);
	
	
	
	/**
	 * The method parses the request data and transforms it into RequisitionDTO
	 * for succeeding micro-services
	 * 
	 * @param json
	 * @return
	 * @throws org.json.simple.parser.ParseException 
	 */
	public RequisitionDTO transformInput(String json) {
			
		RequisitionDTO requisitionDTO = null;
		List<LineItemDTO> lineItems = null;
		
		try {
			
			requisitionDTO = new RequisitionDTO();
			lineItems  = new ArrayList<LineItemDTO>();
			
			JSONParser jsonParser = new JSONParser();
			Object parseObject = jsonParser.parse(json);
			
			JSONObject jsonObject = ((JSONObject)parseObject);
			
			requisitionDTO.setApplicationType(ON_TYPE);
			
			if(jsonObject != null) {
				requisitionDTO.setApplicationTransactionNumber((String)jsonObject.get(UNIQUE_TRANSACTION_NUMBER));
				requisitionDTO.setRequisitionName((String)jsonObject.get(REQUISITION_NAME));
				requisitionDTO.setCo_cd((String)jsonObject.get(COMPANY_CODE));
				requisitionDTO.setRequesterName((String)jsonObject.get(REQUESTER_NAME));
				requisitionDTO.setCommentToSupplier((String)jsonObject.get(COMMENT_TO_SUPPLIER));
				requisitionDTO.setPreparerWebId((String)jsonObject.get(PREPARER_WEB_ID));
				requisitionDTO.setRequesterWebId((String)jsonObject.get(REQUESTER_WEB_ID));
				
				
				Object lineItObject = jsonObject.get(LINE_ITEMS);
				
				if(lineItObject != null) {
					JSONArray lineItList = ((JSONArray)lineItObject);
					for(Object item :lineItList) {
						JSONObject itemObject = ((JSONObject)item);
						populateLineitem(lineItems,itemObject);
					}
				}	
			}			
			
			}catch(org.json.simple.parser.ParseException e) {
				logger.error(e.getMessage());
			}
			
		requisitionDTO.setLineItemDTOs(lineItems);
		
		return requisitionDTO;
	}
	

	
	/**
	 * @param lineItems
	 * @param parlist
	 */
	private void populateLineitem(List<LineItemDTO> lineItems, JSONObject parlist) {
		LineItemDTO itemDTO = new LineItemDTO();
		
		itemDTO.setUnspsc_cd((String)parlist.get(UNSPSC_CD));
		itemDTO.setLineitemDescription((String)parlist.get(LINEITEM_DESCRIPTION));
		itemDTO.setLineItemAmount((String)parlist.get(LINE_ITEM_AMOUNT));
		itemDTO.setLineItemCurrCd((String)parlist.get(LINE_ITEM_CURR_CD));
		itemDTO.setSupplierpartNumber((String)parlist.get(SUPPLIERPART_NUMBER));
		itemDTO.setLineItemUom((String)parlist.get(LINE_ITEM_UOM));
		itemDTO.setSplitPercentage((String)parlist.get(SPLIT_PERCENTAGE));
		itemDTO.setLineItemQty((String)parlist.get(LINE_ITEM_QTY));
		itemDTO.setLineItemSeqNo((String)parlist.get(LINE_ITEM_SEQ_NO));
		itemDTO.setLineitemNeedByDate((String)parlist.get(LINEITEM_NEED_BY_DATE));
		itemDTO.setOriginatingSystemLineItemNumber((String)parlist.get(ORIGINATING_SYSTEM_LINE_ITEM_NUMBER));
		itemDTO.setLineitemSupplierId((String)parlist.get(LINEITEM_SUPPLIER_ID));
		itemDTO.setLineitemContractNo((String)parlist.get(LINEITEM_CONTRACT_NO));
		itemDTO.setLineitemByPassFlag((String)parlist.get(LINEITEM_BY_PASS_FLAG));
		itemDTO.setLineitemSourceCode((String)parlist.get(LINEITEM_SOURCE_CODE));
		itemDTO.setValueOrder((String)parlist.get(VALUE_ORDER));
		itemDTO.setStartDate((String)parlist.get(START_DATE));
		itemDTO.setEndDate((String)parlist.get(END_DATE));
		
		lineItems.add(itemDTO);
	}
	
	
	public static void main(String[] args) {
		String requestData = null;
		
		requestData = "{"
					   + "\"UniqueTransactionNumber\": \"F22345\","
                       + "\"RequisitionName\": \"China Request _Ariba Testing1(F239LY)\","
                       + "\"CompanyCode\": \"0684\","
                       + "\"RequesterName\": \"OneCN Requester\","
                       +"\"CommentToSupplier\": \"Comments to suppliers * Contracts: None\","
                       +"\"PreparerWebId\": \"chinareq1@c25a0161.toronto.ca.ibm.com\","
                       +"\"RequesterWebId\": \"chinareq1@c25a0161.toronto.ca.ibm.com\","
                       +"\"LineItems\": ["
                       +   "{"
					   +      "\"unspsc_cd\": \"81111600\","
					   +      "\"lineitemDescription\": \"Milestone name1~Description of milestone1\","
					   +      "\"lineItemAmount\": \"20.000\","
					   +      "\"lineItemCurrCd\": \"CNY\","
					   +      "\"supplierpartNumber\": \"F239LY000001\","
					   +      "\"lineItemUom\": \"DAY\","
					   +      "\"splitPercentage\": \"100\","
					   +      "\"lineItemQty\": \"3\","
					   +      "\"lineItemSeqNo\": \"1\","
					   +      "\"lineitemNeedByDate\": \"2021-01-02T00:00:00\","
					   +      "\"originatingSystemLineItemNumber\": \"001\","
					   +      "\"lineitemSupplierId\": \"1000301665\","
					   +      "\"lineitemContractNo\": \"null\","
					   +      "\"lineitemByPassFlag\": \"B\","
					   +      "\"lineitemSourceCode\": \"E\","
					   +      "\"valueOrder\": \"false\","
					   +      "\"startDate\": \"2021-01-02T00:00:00\","
					   +      "\"endDate\": \"2022-01-02T00:00:00\""
					   +   "},"
					   +   "{"
					   +      "\"unspsc_cd\": \"81111601\","
					   +      "\"lineitemDescription\": \"Milestone name1~Description of milestone2\","
					   +      "\"lineItemAmount\": \"80.000\","
					   +      "\"lineItemCurrCd\": \"CNY\","
					   +      "\"supplierpartNumber\": \"F239LY000002\","
					   +      "\"lineItemUom\": \"DAY\","
					   +      "\"splitPercentage\": \"101\","
					   +      "\"lineItemQty\": \"3\","
					   +      "\"lineItemSeqNo\": \"1\","
					   +      "\"lineitemNeedByDate\": \"2021-01-02T00:00:00\","
					   +      "\"originatingSystemLineItemNumber\": \"001\","
					   +      "\"lineitemSupplierId\": \"1000301665\","
					   +      "\"lineitemContractNo\": \"null\","
					   +      "\"lineitemByPassFlag\": \"B\","
					   +      "\"lineitemSourceCode\": \"E\","
					   +      "\"valueOrder\": \"false\","
					   +      "\"startDate\": \"2021-01-02T00:00:00\","
					   +      "\"endDate\": \"2022-01-02T00:00:00\""
					   +   "},"
					   +"]"
					   + "}";
		RequisitionDTO dto = new TransformInput().transformInput(requestData);
		
		System.out.println("done" +dto.getLineItemDTOs().size());
	}	

}
