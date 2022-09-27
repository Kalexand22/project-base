package com.axelor.apps.openauctionbase.util;

import java.beans.PropertyDescriptor;
import org.apache.commons.beanutils.PropertyUtils;

public class TransferFields {

  /*
   * Transfer fields from source to target
   * return the target
   */

  public static Object transferFields(Object pSource, Object pDestination) {

    PropertyDescriptor[] objDescriptorsSource = PropertyUtils.getPropertyDescriptors(pSource);
    PropertyDescriptor[] objDescriptorsDestination =
        PropertyUtils.getPropertyDescriptors(pDestination);

    for (PropertyDescriptor objDescriptorSource : objDescriptorsSource) {
      for (PropertyDescriptor objDescriptorDestination : objDescriptorsDestination) {
        if (objDescriptorSource.getName().equals(objDescriptorDestination.getName())
            && !objDescriptorSource.getName().equals("id")
            && !objDescriptorSource.getName().equals("version")
            && !objDescriptorSource.getName().equals("updatedOn")
            && !objDescriptorSource.getName().equals("updatedOn")
            && !objDescriptorSource.getName().equals("createdBy")
            && !objDescriptorSource.getName().equals("createdOn")) {
          try {
            Object objValue = PropertyUtils.getProperty(pSource, objDescriptorSource.getName());
            PropertyUtils.setProperty(pDestination, objDescriptorDestination.getName(), objValue);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
    return pDestination;
  }
}
