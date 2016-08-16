package de.mlu.objects.promo;

import java.util.Properties;

import org.apache.log4j.Logger;

import de.his.dbutils.DBHandler;
import de.his.dbutils.metadata.DBAction;
import de.his.exceptions.HISDateException;
import de.his.tools.DateUtil;
import de.his.tools.QISPropUtil;
import de.his.utils.datetime.HISDate;
import de.mlu.util.MLUUtil;

public class PromotionMed {
	private Logger logger = Logger.getLogger(PromotionMed.class);
	private Properties props = new Properties();

	private Integer promotionid = Integer.valueOf(-1);
	private HISDate date_of_physikum = null;
	private HISDate date_of_pj = null;
	private HISDate eval_ethnical_com_date = null;
	private Integer diss_lang_id = null;
	private Integer vert_lang_id = null;
	private Integer center_of_research_id = Integer.valueOf(0);
	private Integer type_of_dissertation_id = null;
	private Boolean eval_ethnical_com = Boolean.FALSE;
	private Boolean research_training_group = Boolean.FALSE;
	private String research_training_group_detail = null;
	private Boolean statistical_consulting = Boolean.FALSE;
	private String statistical_consulting_detail = null;
	private Boolean prev_promo = Boolean.FALSE;
	private String prev_promo_where = null;
	private Integer study_start_semester = null;
	private Integer study_end_semester = null;

	private DBHandler dbhPromo = null;

	public PromotionMed(Properties promovProp, DBHandler promoDbh) {
		this.props = promovProp;
		this.dbhPromo = promoDbh;
		
		String sourceVar = this.props.getProperty("date_of_physikum");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.date_of_physikum = HISDate.valueOf(sourceVar);
			} catch (HISDateException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("date_of_pj");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.date_of_pj = HISDate.valueOf(sourceVar);
			} catch (HISDateException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("eval_ethnical_com_date");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.eval_ethnical_com_date = HISDate.valueOf(sourceVar);
			} catch (HISDateException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("diss_lang_id");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.diss_lang_id = Integer.valueOf(sourceVar);
			} catch (NumberFormatException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("vert_lang_id");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.vert_lang_id = Integer.valueOf(sourceVar);
			} catch (NumberFormatException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("center_of_research_id");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.center_of_research_id = Integer.valueOf(sourceVar);
			} catch (NumberFormatException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("type_of_dissertation_id");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.type_of_dissertation_id = Integer.valueOf(sourceVar);
			} catch (NumberFormatException e) {logger.error(e.getMessage());}
		}
		this.research_training_group_detail = this.props.getProperty("research_training_group_detail");
		this.statistical_consulting_detail = this.props.getProperty("statistical_consulting_detail");
		if(this.props.getProperty("eval_ethnical_com","N").toUpperCase().equals("J")) {
			this.eval_ethnical_com = Boolean.TRUE;
		}
		if(this.props.getProperty("research_training_group","N").toUpperCase().equals("J")) {
			this.research_training_group = Boolean.TRUE;
		}
		if(this.props.getProperty("statistical_consulting","N").toUpperCase().equals("J")) {
			this.statistical_consulting = Boolean.TRUE;
		}
		if(this.props.getProperty("prev_promo","N").toUpperCase().equals("J")) {
			this.prev_promo = Boolean.TRUE;
		}
		this.prev_promo_where = this.props.getProperty("prev_promo_where");
		sourceVar = this.props.getProperty("study_start_semester");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.study_start_semester = Integer.valueOf(sourceVar);
			} catch (NumberFormatException e) {logger.error(e.getMessage());}
		}
		sourceVar = this.props.getProperty("study_end_semester");
		if(sourceVar != null && !sourceVar.trim().isEmpty()) {
			try {this.study_end_semester = Integer.valueOf(sourceVar);
			} catch (NumberFormatException e) {logger.error(e.getMessage());}
		}
	}
	
	public Boolean saveData(Integer promoId) {
		this.promotionid = promoId;
		Boolean hasSaved = Boolean.TRUE;
		int savedRows = -1;

		Properties saveProp = new Properties();
		QISPropUtil.putIgnoreNull(saveProp, "promotion_id", this.getIntegerString(this.promotionid));
		QISPropUtil.putIgnoreNull(saveProp, "diss_lang_id", this.getIntegerString(this.diss_lang_id));
		QISPropUtil.putIgnoreNull(saveProp, "vert_lang_id", this.getIntegerString(this.vert_lang_id));
		QISPropUtil.putIgnoreNull(saveProp, "center_of_research_id", this.getIntegerString(this.center_of_research_id));
		QISPropUtil.putIgnoreNull(saveProp, "type_of_dissertation_id", this.getIntegerString(this.type_of_dissertation_id));
		QISPropUtil.putIgnoreNull(saveProp, "eval_ethnical_com", this.getBooleanValue(this.eval_ethnical_com));
		QISPropUtil.putIgnoreNull(saveProp, "eval_ethnical_com_date", this.getDateString(this.eval_ethnical_com_date));
		QISPropUtil.putIgnoreNull(saveProp, "research_training_group", this.getBooleanValue(this.research_training_group));
		QISPropUtil.putIgnoreNull(saveProp, "statistical_consulting", this.getBooleanValue(this.statistical_consulting));
		QISPropUtil.putIgnoreNull(saveProp, "research_training_group_detail", MLUUtil.getTransformedString(this.research_training_group_detail));
		QISPropUtil.putIgnoreNull(saveProp, "statistical_consulting_detail", MLUUtil.getTransformedString(this.statistical_consulting_detail));
		QISPropUtil.putIgnoreNull(saveProp, "date_of_physikum", this.getDateString(this.date_of_physikum));
		QISPropUtil.putIgnoreNull(saveProp, "date_of_pj", this.getDateString(this.date_of_pj));
		QISPropUtil.putIgnoreNull(saveProp, "prev_promo", this.getBooleanValue(this.prev_promo));
		QISPropUtil.putIgnoreNull(saveProp, "prev_promo_where", MLUUtil.getTransformedString(this.prev_promo_where));
		QISPropUtil.putIgnoreNull(saveProp, "study_start_semester", this.getIntegerString(this.study_start_semester));
		QISPropUtil.putIgnoreNull(saveProp, "study_end_semester", this.getIntegerString(this.study_end_semester));

		saveProp.put("zeitstempel", DateUtil.getKeyWordForTimestamp(this.dbhPromo));
		savedRows = this.dbhPromo.execUpdate(DBAction.INSERT, "promotion_med", saveProp, null);
		if(savedRows < 1) {
			hasSaved = Boolean.FALSE;
		}
		return hasSaved;
	}

	private String getIntegerString(Integer value) {
		String returnValue = null;
		if(value != null) {
			returnValue = value.toString();
		}
		return returnValue;
	}
	
	private String getDateString(HISDate date) {
		String returnValue = null;
		if(date != null) {
			returnValue = date.toSQLString();
		}
		return returnValue;
	}
	
	private String getBooleanValue(Boolean bool) {
		String returnValue = "J";
		if(bool.equals(Boolean.FALSE)) {
			returnValue = "N";
		}
		return returnValue;
	}

	public void setPromotionId(Integer promoId) {
		this.promotionid = promoId;
	}
	
	public Integer getPromotionId() {
		return this.promotionid;
	}
}
