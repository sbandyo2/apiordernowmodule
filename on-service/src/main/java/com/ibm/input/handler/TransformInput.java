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

	private static final String ORIGINATING_SYSTEM_LINE_ITEM_NUMBER = "OriginatingSystemLineNumber";

	private static final String LINEITEM_NEED_BY_DATE = "lineitemNeedByDate";

	private static final String LINE_ITEM_SEQ_NO = "NumberInCollection";

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

	private static final String UNIQUE_TRANSACTION_NUMBER = "UniqueName";
	
	private static final String ON_TYPE = "ORDERNOW";
	
	private static final String COST_CENTRE = "CostCenter";
	private static final String WBSELEMENT = "WBSElement";
	private static final String MANPARTNUMBER = "ManPartNumber";
	private static final String SHIPTO = "ShipTo";
	private static final String PURCHASEORG = "PurchOrg";

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
		
		if(parlist.containsKey(COST_CENTRE)) {
			itemDTO.setCostCentre((String)parlist.get(COST_CENTRE));
		}
		if(parlist.containsKey(WBSELEMENT)) {
			itemDTO.setWbsElement((String)parlist.get(WBSELEMENT));
		}
		
		if(parlist.containsKey(MANPARTNUMBER)) {
			itemDTO.setManPartNumber((String)parlist.get(MANPARTNUMBER));
		}
		
		if(parlist.containsKey(SHIPTO)) {
			itemDTO.setShipTo((String)parlist.get(SHIPTO));
		}
		
		if(parlist.containsKey(PURCHASEORG)) {
			itemDTO.setPurchaseOrg((String)parlist.get(PURCHASEORG));
		}
		
		lineItems.add(itemDTO);
	}
	
	
	public static void main(String[] args) {
		String requestData = null;
		
		requestData = "{" 
  + "\"UniqueName\": \"35006025\","
  + "\"RequisitionName\": \"ON1126265 - TestCustomerABC12\","
  + "\"CompanyCode\": \"0147\","
  + "\"CommentToSupplier\": \"This is a test request to see how the OrderNow OP data can be provided to Ariba through a new application program.\","
  + "\"PreparerWebId\": \"csafpreq2@c25a0161.toronto.ca.ibm.com\","
  + "\"RequesterWebId\": \"csafpreq2@c25a0161.toronto.ca.ibm.com\","
  + "\"LineItems\": [ " 
    +"{"
    + "\"unspsc_cd\": \"81111600\"," 
    + " \"lineitemDescription\": \"RedHat Enterprise Linux for Virtual Data\","
    + " \"lineItemAmount\": \"2000.000\","
    + " \"lineItemCurrCd\": \"USD\","
    + "\"supplierpartNumber\": \"RH00077\","
    + "\"lineItemUom\": \"EA\","
    + "\"splitPercentage\": \"100\","
    + "\"lineItemQty\": \"4\","
    + "\"NumberInCollection\": \"1\","
    + "\"lineitemNeedByDate\": \"2019-04-01T00:00:00\","
    + "\"OriginatingSystemLineNumber\": \"001\","
    + "\"lineitemSupplierId\": \"1000120951\","
    + "\"startDate\": \"2019-04-01T00:00:00\","
    + "\"endDate\": \"2020-03-31T00:00:00\","
    + "\"ManPartNumber\": \"RH00077\","
    + "\"CostCenter\": \"USDF0147\","
    + "\"ShipTo\": \"AD-FB7\","
    + "\"PurchOrg\": \"K000\""
    + "}"
    + "{"
    + "\"unspsc_cd\": \"81111600\","
    + "\"lineitemDescription\": \"RedHat Enterprise Linux for Virtual Data\","
    + "\"lineItemAmount\": \"2000.000\","
    + "\"lineItemCurrCd\": \"USD\","
    + "\"supplierpartNumber\": \"RH00077\","
    + "\"lineItemUom\": \"EA\","
    + "\"splitPercentage\": \"100\","
    + "\"lineItemQty\": \"14\","
    + "\"NumberInCollection\": \"2\","
    + "\"lineitemNeedByDate\": \"2019-04-01T00:00:00\","
    + "\"OriginatingSystemLineNumber\": \"001\","
    + "\"lineitemSupplierId\": \"1000120951\","
    + "\"startDate\": \"2019-04-01T00:00:00\","
    + "\"endDate\": \"2020-03-31T00:00:00\","
    + "\"ManPartNumber\": \"RH00077\","
    + "\"WBSElement\": \"WK91R\","
    + "\"ShipTo\": \"AD-FB7\","
    + "\"PurchOrg\": \"K000\""
    + "}"
    + "{"
        + "\"unspsc_cd\": \"81111600\","
        + "\"lineitemDescription\": \"RedHat Enterprise Linux for Virtual Data\","
        + "\"lineItemAmount\": \"2000.000\","
        + "\"lineItemCurrCd\": \"USD\","
        + "\"supplierpartNumber\": \"RH00077\","
        + "\"lineItemUom\": \"EA\","
        + "\"splitPercentage\": \"100\","
        + "\"lineItemQty\": \"2\","
        + "\"NumberInCollection\": \"3\","
        + "\"lineitemNeedByDate\": \"2019-04-01T00:00:00\","
        + "\"OriginatingSystemLineNumber\": \"001\","
        + "\"lineitemSupplierId\": \"1000120951\","
        + "\"startDate\": \"2019-04-01T00:00:00\","
        + "\"endDate\": \"2020-03-31T00:00:00\","
        + "\"ManPartNumber\": \"RH00077\","
        + "\"CostCenter\": \"USDF0147\","
        + "\"ShipTo\": \"AD-FB7\","
        + "\"PurchOrg\": \"K000\""
      + "}"
      + "{"
      + "\"unspsc_cd\": \"81111600\","
      + "\"lineitemDescription\": \"RedHat Enterprise Linux for Virtual Data\","
      + "\"lineItemAmount\": \"2000.000\","
      + "\"lineItemCurrCd\": \"USD\","
      + "\"supplierpartNumber\": \"RH00077\","
      + "\"lineItemUom\": \"EA\","
      + "\"splitPercentage\": \"100\","
      + "\"lineItemQty\": \"8\","
      + "\"NumberInCollection\": \"4\","
      + "\"lineitemNeedByDate\": \"2019-04-01T00:00:00\","
      + "\"OriginatingSystemLineNumber\": \"001\","
      + "\"lineitemSupplierId\": \"1000120951\","
      + "\"startDate\": \"2019-04-01T00:00:00\","
      + "\"endDate\": \"2020-03-31T00:00:00\","
      + "\"ManPartNumber\": \"RH00077\","
      + "\"WBSElement\": \"WK91R\","
      + "\"ShipTo\": \"AD-FB7\","
      + "\"PurchOrg\": \"K000\""
      +"}"
    
+"]"
+"}";
		RequisitionDTO dto = null;
		
		try {
			dto = new TransformInput().transformInput(requestData);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("done" +dto.getLineItemDTOs().size());
	}	

}
