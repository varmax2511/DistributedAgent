package com.varun.agent.config.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Model class for the meta-data information for the particular collection.
 * 
 * @author varunjai
 *
 */
@XmlRootElement(name = "meta-data")
@XmlType(propOrder = {"product", "submitterName", "submitterEmail"})
public class Metadata {
  /**
   * 
   */
  private String product;
  /**
   * 
   */
  private String submitterName;
  /**
   * 
   */
  private String submitterEmail;

  /**
   * Get the product name.
   * 
   * @return
   */
  @XmlElement(name = "product")
  public String getProduct() {
    return product;
  }
  /**
   * see {@link #getProduct()}
   * 
   * @param product
   */
  public void setProduct(String product) {
    this.product = product;
  }
  /**
   * Get the submitter name.
   * 
   * @return
   */
  @XmlElement(name = "submitter-name")
  public String getSubmitterName() {
    return submitterName;
  }
  /**
   * see {@link #getSubmitterName()}
   * 
   * @param submitterName
   */
  public void setSubmitterName(String submitterName) {
    this.submitterName = submitterName;
  }
  /**
   * Get the submitter email.
   * 
   * @return
   */
  @XmlElement(name = "submitter-email")
  public String getSubmitterEmail() {
    return submitterEmail;
  }
  /**
   * see {@link #getSubmitterEmail()}
   * 
   * @param submitterEmail
   */
  public void setSubmitterEmail(String submitterEmail) {
    this.submitterEmail = submitterEmail;
  }

}
