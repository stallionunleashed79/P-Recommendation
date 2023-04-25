<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method='html' version='1.0' encoding='UTF-8' indent='yes'/>
  <xsl:variable name="logoHeader">
    <![CDATA[
    <img src="data:image/png;base64,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"
          width="122px" height="79px" />
    ]]>
  </xsl:variable>
  <xsl:variable name="logoAsset">
    <![CDATA[
    <img src="data:image/png;base64,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"
          width="38px" height="20px" />
    ]]>
  </xsl:variable>
  <xsl:template match="/">
    <html>
      <style type="text/css">
        /* cyrillic-ext */
        @font-face {
        font-family: 'Noto Sans';
        font-style: normal;
        font-weight: 400;
        src: url(https://fonts.gstatic.com/s/notosans/v11/o-0IIpQlx3QUlC5A4PNr6DRAW_0.woff2) format('woff2');
        unicode-range: U+0460-052F, U+1C80-1C88, U+20B4, U+2DE0-2DFF, U+A640-A69F, U+FE2E-FE2F;
        }
        /* cyrillic */
        @font-face {
        font-family: 'Noto Sans';
        font-style: normal;
        font-weight: 400;
        src: url(https://fonts.gstatic.com/s/notosans/v11/o-0IIpQlx3QUlC5A4PNr4TRAW_0.woff2) format('woff2');
        unicode-range: U+0400-045F, U+0490-0491, U+04B0-04B1, U+2116;
        }
        /* devanagari */
        @font-face {
        font-family: 'Noto Sans';
        font-style: normal;
        font-weight: 400;
        src: url(https://fonts.gstatic.com/s/notosans/v11/o-0IIpQlx3QUlC5A4PNr5DRAW_0.woff2) format('woff2');
        unicode-range: U+0900-097F, U+1CD0-1CF6, U+1CF8-1CF9, U+200C-200D, U+20A8, U+20B9, U+25CC, U+A830-A839, U+A8E0-A8FB;
        }
        /* greek-ext */
        @font-face {
        font-family: 'Noto Sans';
        font-style: normal;
        font-weight: 400;
        src: url(https://fonts.gstatic.com/s/notosans/v11/o-0IIpQlx3QUlC5A4PNr6TRAW_0.woff2) format('woff2');
        unicode-range: U+1F00-1FFF;
        }
        /* greek */
        @font-face {
        font-family: 'Noto Sans';
        font-style: normal;
        font-weight: 400;
        src: url(https://fonts.gstatic.com/s/notosans/v11/o-0IIpQlx3QUlC5A4PNr5jRAW_0.woff2) format('woff2');
        unicode-range: U+0370-03FF;
        }
        /* vietnamese */
        @font-face {
        font-family: 'Noto Sans';
        font-style: normal;
        font-weight: 400;
        src: url(https://fonts.gstatic.com/s/notosans/v11/o-0IIpQlx3QUlC5A4PNr6jRAW_0.woff2) format('woff2');
        unicode-range: U+0102-0103, U+0110-0111, U+0128-0129, U+0168-0169, U+01A0-01A1, U+01AF-01B0, U+1EA0-1EF9, U+20AB;
        }
        /* latin-ext */
        @font-face {
        font-family: 'Noto Sans';
        font-style: normal;
        font-weight: 400;
        src: url(https://fonts.gstatic.com/s/notosans/v11/o-0IIpQlx3QUlC5A4PNr6zRAW_0.woff2) format('woff2');
        unicode-range: U+0100-024F, U+0259, U+1E00-1EFF, U+2020, U+20A0-20AB, U+20AD-20CF, U+2113, U+2C60-2C7F, U+A720-A7FF;
        }
        /* latin */
        @font-face {
        font-family: 'Noto Sans';
        font-style: normal;
        font-weight: 400;
        src: url(https://fonts.gstatic.com/s/notosans/v11/o-0IIpQlx3QUlC5A4PNr5TRA.woff2) format('woff2');
        unicode-range: U+0000-00FF, U+0131, U+0152-0153, U+02BB-02BC, U+02C6, U+02DA, U+02DC, U+2000-206F, U+2074, U+20AC, U+2122, U+2191, U+2193, U+2212, U+2215, U+FEFF, U+FFFD;
        }

        body {
        font-size: 12px;
        font-family: Noto Sans;
        font-style: normal;
        text-align: justify;
        }

        td {
        text-align: left;
        font-size: 12px;
        }

        .format_table {
        width: 697.5px;
        display: inline-block;
        }

        .line_break {
        height: 10px;
        border-bottom: 0.5px grey;
        }

        .asset-header-container {
        font-weight: 800;
        font-size: 19px;
        margin-button: 20px;
        }
        .img-container {
        float: right;
        display: inline-block;
        }
        .asset_image_container{
        float: left;
        }
        .header-container {
        width: 418px;
        background-color: #E6E6E6;
        display: block;
        }
        .titles {
        font-size: 14px;
        }
        .my_field_label, .titles {
        font-weight: 800;
        }



        .divider {
        height: 0.5px;
        background-color: #43485C;
        }

        .table_separator {
        border-bottom: 15px #E6E9F0 solid;
        }

        .divider_degraded {
        height: 0.5px;
        background-color: #E6E6E6;
        }

        .links-attachments-column {
          padding: 0 15px 0 15px;
          width: 50%;
          text-align: left;
         }
         tr.links-attachments-row td, tr.links-attachments-row th {
          border-bottom: 1px lightgrey;
          padding-bottom: 5px;
        }
        .links-attachments-table {
          width: 100%;
          margin-right: 30px;
          border-collapse: collapse;
        }

        .links-and-attachments-title p {
          margin-left: 5px;
        }
      </style>
      <body>
        <div class="header">
          <table class="format_table">
            <tr style="height: 19px;">
              <td colspan="3" class="asset-header-container">
                <span><xsl:value-of select="RecommendationDetailsResponse/commonFields/assetMetadata/make"/> -
                  <xsl:value-of select="RecommendationDetailsResponse/commonFields/assetMetadata/model"/>
                </span>
                <span> | </span>
                <span><xsl:value-of select="RecommendationDetailsResponse/commonFields/assetMetadata/serialNumber"/></span>
                <span> | </span>
                <span><xsl:value-of select="RecommendationDetailsResponse/commonFields/assetMetadata/name"/></span>
              </td>
              <td rowspan="4" style="width: 127px, height: 79px">
                <xsl:value-of select="$logoHeader" disable-output-escaping="yes"/>
              </td>
            </tr>
            <tr style="height: 21px;">
              <td colspan="3"> </td>
            </tr>
            <tr style="height: 8px;">
              <td rowspan="3" style="width: 43px">
                <xsl:value-of select="$logoAsset" disable-output-escaping="yes"/>
              </td>
              <td style="width: 200px">
                <span class="my_field_label">SMU:</span>
                <span class="my_field_content">
                  <xsl:text> </xsl:text><xsl:value-of select="RecommendationDetailsResponse/commonFields/hoursReading/reading"/>
                </span>
              </td>
              <td style="width: 454.5px">
                <span class="my_field_label">Priority:</span>
                <span class="my_field_content">
                  <xsl:text> </xsl:text>
                  <xsl:for-each select="RecommendationDetailsResponse/templateCustomProperties/templateCustomProperties[propertyName='recommendationPriority']">
                    <xsl:value-of select="propertyValue"/>
                  </xsl:for-each>
                </span>
              </td>
            </tr>
            <tr style="height: 8px;">
              <td>
                <span class="my_field_label">Last SMU Date:</span>
                <span class="my_field_content">
                  <xsl:text> </xsl:text><xsl:value-of select="RecommendationDetailsResponse/commonFields/hoursReading/readingSmuDate"/>
                </span>
              </td>
              <td>
                <span class="my_field_label">Expiration Date:</span>
                <span class="my_field_content">
                  <xsl:text> </xsl:text><xsl:value-of select="RecommendationDetailsResponse/commonFields/expirationTime/monthValue"/>/<xsl:value-of select="RecommendationDetailsResponse/commonFields/expirationTime/dayOfMonth"/>/<xsl:value-of select="RecommendationDetailsResponse/commonFields/expirationTime/year"/>
                </span>
              </td>
            </tr>
            <tr style="height: 8px;">
              <td>
                <span class="my_field_label">Created on:</span>
                <span class="my_field_content">
                  <xsl:text> </xsl:text><xsl:value-of select="RecommendationDetailsResponse/commonFields/createdTime/monthValue"/>/<xsl:value-of select="RecommendationDetailsResponse/commonFields/createdTime/dayOfMonth"/>/<xsl:value-of select="RecommendationDetailsResponse/commonFields/createdTime/year"/>
                </span>
              </td>
              <td colspan="2">
                <span class="my_field_label">Recommendation No.:</span>
                <span class="my_field_content"><xsl:text> </xsl:text><xsl:value-of select="RecommendationDetailsResponse/recommendationNumber"/></span>
              </td>
            </tr>
            <tr class="line_break">
              <td colspan="4"> </td>
            </tr>
          </table>
        </div>



        <!--divider-->
        <div class="divider"> </div>

        <!-- start title of the recommendation-->
        <div>
          <table class="format_table">
            <tr class="line_break">
              <td> </td>
            </tr>
            <tr>
              <td>
                <span class="titles"><xsl:value-of select="RecommendationDetailsResponse/commonFields/title"/></span>
              </td>
            </tr>
            <tr>
              <td>
                <span class="my_field_label">Created By: </span>
                <span class="my_field_content">
                  <xsl:value-of select="RecommendationDetailsResponse/commonFields/createdBy/firstName"/><xsl:text> </xsl:text><xsl:value-of select="RecommendationDetailsResponse/commonFields/createdBy/lastName"/>
                </span>
              </td>
            </tr>
            <tr class="line_break">
              <td> </td>
            </tr>
          </table>
        </div>
        <div class="divider_degraded"></div>

        <!-- start recommended action-->
        <div>
          <table class="format_table">
            <tr class="line_break">
              <td> </td>
            </tr>
            <tr>
              <td>
                <span class="titles">RECOMMENDED ACTION</span><br>line break</br>
              </td>
            </tr>
            <tr>
              <td>
                <span class="my_field_content">
                  <xsl:text> </xsl:text>
                  <xsl:for-each select="RecommendationDetailsResponse/templateCustomProperties/templateCustomProperties[propertyName='recommendationAction']">
                    <xsl:value-of select="propertyValue" disable-output-escaping="yes"/>
                  </xsl:for-each>
                </span>
              </td>
            </tr>
            <tr class="line_break">
              <td> </td>
            </tr>
          </table>
        </div>
        <div class="divider_degraded"></div>

        <!-- start description -->
        <div>
          <table class="format_table">
            <tr class="line_break">
              <td> </td>
            </tr>
            <tr>
              <td>
                <span class="titles">DESCRIPTION</span>
              </td>
            </tr>
            <tr>
              <td>
                <span class="my_field_content">
                  <xsl:text> </xsl:text>
                  <xsl:for-each select="RecommendationDetailsResponse/templateCustomProperties/templateCustomProperties[propertyName='recommendationDetails']">
                    <xsl:value-of select="propertyValue" disable-output-escaping="yes"/>
                  </xsl:for-each>
                </span>
              </td>
            </tr>
            <tr class="line_break">
              <td> </td>
            </tr>
          </table>
        </div>

        <div class="divider_degraded"></div>

        <!-- start Links and attachments -->
        <xsl:variable name="vLower" select="'abcdefghijklmnopqrstuvwxyz'"/>
        <xsl:variable name="vUpper" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'"/>
        <xsl:if test="count(RecommendationDetailsResponse/attachments/attachments) > '0' or
        count(RecommendationDetailsResponse/links/links) > '0'">
        <div class="links-and-attachments-title">
          <p><span class="titles"><b>LINKS AND ATTACHMENTS</b></span></p>
          <xsl:if test="count(RecommendationDetailsResponse/attachments/attachments) > '0'">
          <table class="links-attachments-table">
            <tr class="links-attachments-row">
              <th class="links-attachments-column">Existing Files</th>
              <th class="links-attachments-column">Date Attached</th>
            </tr>
            <tr class="line_break">
              <td colspan="2"> </td>
            </tr>
            <xsl:for-each select="RecommendationDetailsResponse/attachments/attachments">
              <tr class="links-attachments-row">
                <td class="links-attachments-column">
                  <b><xsl:value-of select="name"/></b>
                </td>
                <td class="links-attachments-column">
                  <xsl:value-of select="translate(substring(fileAttachedTime/month ,1,1), $vLower, $vUpper)"/>
                  <xsl:value-of select="translate(substring(fileAttachedTime/month ,2), $vUpper, $vLower)"/>
                  &#160;<xsl:value-of select="fileAttachedTime/dayOfMonth"/>,
                  &#160;<xsl:value-of select="fileAttachedTime/year"/>
                </td>
              </tr>
              <tr class="line_break">
                <td colspan="2"> </td>
              </tr>
            </xsl:for-each>
          </table>
          <br>line break</br>
          </xsl:if>
          <xsl:if test="count(RecommendationDetailsResponse/links/links) > '0'">
          <table class="links-attachments-table">
            <tr class="links-attachments-row">
              <th class="links-attachments-column">Existing Links</th>
              <th class="links-attachments-column">Date Attached</th>
            </tr>
            <tr class="line_break">
              <td colspan="2"> </td>
            </tr>
            <xsl:for-each select="RecommendationDetailsResponse/links/links">
              <tr class="links-attachments-row">
                <td class="links-attachments-column">
                  <a href="{@URL}"><xsl:value-of select="url"/></a>
                </td>
                <td class="links-attachments-column">
                    <xsl:value-of select="translate(substring(attachedDate/month ,1,1), $vLower, $vUpper)"/>
                    <xsl:value-of select="translate(substring(attachedDate/month ,2), $vUpper, $vLower)"/>
                  &#160;<xsl:value-of select="attachedDate/dayOfMonth"/>,
                  &#160;<xsl:value-of select="attachedDate/year"/>
                </td>
              </tr>
              <tr class="line_break">
                <td colspan="2"> </td>
              </tr>
            </xsl:for-each>
            <tr class="line_break">
              <td> </td>
            </tr>
          </table>
          </xsl:if>
        </div>
        </xsl:if>
        <div class="divider_degraded"></div>

        <!-- start value estimator -->
        <div>
          <table class="format_table">
            <tr class="line_break">
              <td colspan="2"> </td>
            </tr>
            <tr>
              <td>
                <span class="titles">VALUE ESTIMATOR</span>
              </td>
            </tr>
            <tr class="line_break">
              <td colspan="2"> </td>
            </tr>
            <tr>
              <td>
                <span class="my_field_label">Currency:</span>
                <span class="my_field_content">
                  <xsl:text> </xsl:text>
                  <xsl:for-each select="RecommendationDetailsResponse/templateCustomProperties/templateCustomProperties[propertyName='valueEstimateCurrency']">
                    <xsl:value-of select="propertyValue"/>
                  </xsl:for-each>
                </span>
              </td>
              <td>
                <span class="my_field_label">Repair Cost:</span>
                <span class="my_field_content">
                  <xsl:text> </xsl:text>
                  <xsl:for-each select="RecommendationDetailsResponse/templateCustomProperties/templateCustomProperties[propertyName='valueEstimateRepairCost']">
                    <xsl:value-of select="propertyValue"/>
                  </xsl:for-each>
                </span>
              </td>
            </tr>
            <tr class="line_break">
              <td> </td>
            </tr>
            <tr>
              <td>
                <span class="my_field_label">Estimated Failure Cost:</span>
                <span class="my_field_content">
                  <xsl:text> </xsl:text>
                  <xsl:for-each select="RecommendationDetailsResponse/templateCustomProperties/templateCustomProperties[propertyName='valueEstimateFailureCost']">
                    <xsl:value-of select="propertyValue"/>
                  </xsl:for-each>
                </span>
              </td>
              <td>
                <span class="my_field_label">Recommendation Value:</span>
                <span class="my_field_content">
                  <xsl:text> </xsl:text>
                  <xsl:for-each select="RecommendationDetailsResponse/templateCustomProperties/templateCustomProperties[propertyName='valueEstimateRecommendationValue']">
                    <xsl:value-of select="propertyValue"/>
                  </xsl:for-each>
                </span>
              </td>
            </tr>
            <tr class="line_break">
              <td> </td>
            </tr>
            <tr>
              <td colspan="2">
                <span class="my_field_content">
                  <xsl:for-each select="RecommendationDetailsResponse/templateCustomProperties/templateCustomProperties[propertyName='valueEstimateDescription']">
                    <xsl:value-of select="propertyValue" disable-output-escaping="yes"/>
                  </xsl:for-each>
                </span>
              </td>
            </tr>
            <tr class="line_break">
              <td> </td>
            </tr>
          </table>
        </div>

        <div class="divider_degraded"></div>
        <p><span class="titles">RELATED EXCEPTIONS</span></p>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>