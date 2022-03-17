<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl js"
  xmlns="http://www.w3.org/1999/xhtml" xmlns:js="urn:custom-javascript"
  xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
  <xsl:output method="html" indent="YES"/>
  <xsl:variable name="itemsPerPage">
    <xsl:value-of select="10"/>
  </xsl:variable>
  <xsl:variable name="itemCount">
    <xsl:value-of select="count(Invoice//Content//Products//Product)"/>
  </xsl:variable>
  <xsl:variable name="pagesNeeded">
    <xsl:choose>
      <xsl:when test="$itemCount &lt;= $itemsPerPage">
        <xsl:value-of select="1"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:choose>
          <xsl:when test="$itemCount mod $itemsPerPage = 0">
            <xsl:value-of select="$itemCount div $itemsPerPage"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="ceiling($itemCount div $itemsPerPage)"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:variable>


  <xsl:variable name="PaymentMethods">
    <PaymentMethod>VND</PaymentMethod>
    <PaymentMethod>Visa</PaymentMethod>
    <PaymentMethod>Master</PaymentMethod>
    <PaymentMethod>Amex</PaymentMethod>
    <PaymentMethod>Diners</PaymentMethod>
    <PaymentMethod>JCB</PaymentMethod>
    <PaymentMethod>ENT Payment</PaymentMethod>
    <PaymentMethod>C/Ledger</PaymentMethod>
    <PaymentMethod>Complimentar</PaymentMethod>
    <PaymentMethod>Room Charge</PaymentMethod>
    <PaymentMethod>Packet VC</PaymentMethod>
    <PaymentMethod>FOC</PaymentMethod>
    <PaymentMethod>US Offline</PaymentMethod>
    <PaymentMethod>VND Off=>US$</PaymentMethod>
    <PaymentMethod>R.Charge OFF</PaymentMethod>
  </xsl:variable>

  <xsl:template name="arrayContains">
   
  </xsl:template>
  
<!--  Spit string to Items (c) Chuong -->
  <xsl:template name="splitStringToItems">
    <xsl:param name="list" />
    <xsl:param name="delimiter" select="','"  />
    <xsl:variable name="_delimiter">
      <xsl:choose>
        <xsl:when test="string-length($delimiter)=0">,</xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$delimiter"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="newlist">
      <xsl:choose>
        <xsl:when test="contains($list, $_delimiter)">
          <xsl:value-of select="normalize-space($list)" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="concat(normalize-space($list), $_delimiter)"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="first" select="substring-before($newlist, $_delimiter)" />
    <xsl:variable name="remaining" select="substring-after($newlist, $_delimiter)" />
    <item>
      <xsl:value-of select="$first" />
    </item>
    <xsl:if test="$remaining">
      <xsl:call-template name="splitStringToItems">
        <xsl:with-param name="list" select="$remaining" />
        <xsl:with-param name="delimiter" select="$_delimiter" />
      </xsl:call-template>
    </xsl:if>
  </xsl:template>

  <xsl:template name="addZero">
    <xsl:param name="count"/>
    <xsl:if test="$count > 0">
      <xsl:text>0</xsl:text>
      <xsl:call-template name="addZero">
        <xsl:with-param name="count" select="$count - 1"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>
  <xsl:template name="addDots">
    <xsl:param name="val"/>
    <xsl:param name="val1"/>
    <xsl:param name="val2"/>
    <xsl:param name="i" select="1"/>
    <xsl:if test="$val1 > 0">
      <xsl:choose>
        <xsl:when test="$val2 != 0">
          <xsl:value-of select="substring($val, $i, $val2)"/>
          <xsl:if test="substring($val, $i + $val2 + 1, 1) != ''">
            <xsl:text>.</xsl:text>
          </xsl:if>
          <xsl:call-template name="addDots">
            <xsl:with-param name="val" select="$val"/>
            <xsl:with-param name="val1" select="$val1 - 1"/>
            <xsl:with-param name="i" select="$i + $val2"/>
            <xsl:with-param name="val2" select="3"/>
          </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
          <!--<xsl:text>test</xsl:text>-->
          <xsl:value-of select="substring($val, $i, 3)"/>
          <xsl:if test="substring($val, $i + 3, 1) != ''">
            <xsl:text>.</xsl:text>
          </xsl:if>
          <xsl:call-template name="addDots">
            <xsl:with-param name="val" select="$val"/>
            <xsl:with-param name="val1" select="$val1 - 1"/>
            <xsl:with-param name="i" select="$i + 3"/>
            <xsl:with-param name="val2" select="3"/>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:if>
  </xsl:template>


  <xsl:template name="findSpaceChar">
    <xsl:param name="str"/>
    <xsl:variable name="strLength">
      <xsl:value-of select="string-length($str)"/>
    </xsl:variable>
    <xsl:if test="$strLength &gt; 0">
      <xsl:choose>
        <xsl:when test="substring($str, $strLength) != ' '">
          <xsl:call-template name="findSpaceChar">
            <xsl:with-param name="str" select="substring($str, 1, $strLength - 1)"/>
          </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$strLength"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:if>
  </xsl:template>

  <xsl:template name="tempCusName">
    <xsl:param name="str"/>
    <xsl:variable name="strLength">
      <xsl:value-of select="string-length($str)"/>
    </xsl:variable>
    <xsl:variable name="row1Length">
      <xsl:value-of select="85"/>
    </xsl:variable>
    <xsl:choose>
      <xsl:when test="$strLength &gt; $row1Length">
        <xsl:variable name="str0">
          <xsl:value-of select="substring($str, 1, $row1Length)"/>
        </xsl:variable>
        <xsl:variable name="spaceCharPosition">
          <xsl:call-template name="findSpaceChar">
            <xsl:with-param name="str" select="$str0"/>
          </xsl:call-template>
        </xsl:variable>
        <xsl:variable name="str1">
          <xsl:value-of select="substring($str0, 1, $spaceCharPosition)"/>
        </xsl:variable>
        <xsl:variable name="str2">
          <xsl:value-of select="substring($str, $spaceCharPosition + 1)"/>
        </xsl:variable>
        <div class="clsTable">
          <div class="clsCol col-title">
            <p style="font-family: Arial;"> Tên khách <i>(Guest Name)</i>
            </p>
          </div>
          <div class="clsCol col-txt">
            <p class="input-txt" style=""> :&#160;<xsl:value-of select="$str1"/>
            </p>
          </div>
        </div>
        <div class="clsTable">
          <div class="clsCol col-txt">
            <p class="input-txt" style="">
              <xsl:value-of select="$str2"/>
            </p>
          </div>
        </div>
      </xsl:when>
      <xsl:otherwise>
        <div class="clsTable">
          <div class="clsCol col-title">
            <p style="font-family: Arial;"> Tên khách<i>(Guest Name)</i>
            </p>
          </div>
          <div class="clsCol col-txt">
            <p class="input-txt" style=""> :&#160;<xsl:value-of select="$str"/>
            </p>
          </div>
        </div>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>


  <xsl:template name="tempCusAddress">
    <xsl:param name="str"/>
    <xsl:variable name="strLength">
      <xsl:value-of select="string-length($str)"/>
    </xsl:variable>
    <xsl:variable name="row1Length">
      <xsl:value-of select="48"/>
    </xsl:variable>
    <xsl:choose>
      <xsl:when test="$strLength &gt; $row1Length">
        <xsl:variable name="str0">
          <xsl:value-of select="substring($str, 1, $row1Length)"/>
        </xsl:variable>
        <xsl:variable name="spaceCharPosition">
          <xsl:call-template name="findSpaceChar">
            <xsl:with-param name="str" select="$str0"/>
          </xsl:call-template>
        </xsl:variable>
        <xsl:variable name="str1">
          <xsl:value-of select="substring($str0, 1, $spaceCharPosition)"/>
        </xsl:variable>
        <xsl:variable name="str2">
          <xsl:value-of select="substring($str, $spaceCharPosition + 1)"/>
        </xsl:variable>
        <div class="clsTable">
          <div class="clsCol col-title">
            <p style="font-family: Arial;"> Địa chỉ <i>(Address)</i>
            </p>
          </div>
          <div class="clsCol col-txt">
            <p class="input-txt" style=""> :&#160;<xsl:value-of select="$str1"/>
            </p>
          </div>
        </div>

        <div class="clsTable">
          <div class="clsCol col-txt" style="    width: 100%;float: left;">
            <p class="input-txt" style="margin-left: 97px;"> &#160;<xsl:value-of select="$str2"/>
            </p>
          </div>

        </div>
      </xsl:when>
      <xsl:otherwise>

        <div class="clsTable">
          <div class="clsCol col-title">
            <p style="font-family: Arial;"> Địa chỉ <i>(Address)</i>
            </p>
          </div>
          <div class="clsCol col-txt">
            <p class="input-txt"> :&#160;<xsl:value-of select="$str"/>
            </p>
          </div>
        </div>

      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>


  <xsl:template name="tempBuyer">
    <xsl:param name="str"/>
    <xsl:variable name="strLength">
      <xsl:value-of select="string-length($str)"/>
    </xsl:variable>
    <xsl:variable name="row1Length">
      <xsl:value-of select="47"/>
    </xsl:variable>
    <xsl:choose>
      <xsl:when test="$strLength &gt; $row1Length">
        <xsl:variable name="str0">
          <xsl:value-of select="substring($str, 1, $row1Length)"/>
        </xsl:variable>
        <xsl:variable name="spaceCharPosition">
          <xsl:call-template name="findSpaceChar">
            <xsl:with-param name="str" select="$str0"/>
          </xsl:call-template>
        </xsl:variable>
        <xsl:variable name="str1">
          <xsl:value-of select="substring($str0, 1, $spaceCharPosition)"/>
        </xsl:variable>
        <xsl:variable name="str2">
          <xsl:value-of select="substring($str, $spaceCharPosition + 1)"/>
        </xsl:variable>
        <div class="clsTable">
          <div class="clsCol col-title">
            <p style="font-family: Arial;"> Tên đơn vị (<i>Company</i>) </p>
          </div>
          <div class="clsCol col-txt">
            <p class="input-txt" style=""> :&#160;<xsl:value-of select="$str1"/>
            </p>
          </div>
        </div>

        <div class="clsTable">
          <div class="clsCol col-txt" style="    width: 100%;float: left;">
            <p class="input-txt" style="    margin-left: 125px;"> &#160;<xsl:value-of select="$str2"
              />
            </p>
          </div>
        </div>
      </xsl:when>
      <xsl:otherwise>

        <div class="clsTable">
          <div class="clsCol col-title">
            <p style="font-family: Arial;"> Tên đơn vị <i>(Company)</i>
            </p>
          </div>
          <div class="clsCol col-txt">
            <p class="input-txt" style=""> :&#160;<xsl:value-of select="$str"/>
            </p>
          </div>
        </div>

      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="addLine">
    <xsl:param name="count"/>
    <xsl:if test="$count > 0">
      <tr class="noline back" style="    border-top: #000 dotted 0px ">
        <td style="border-bottom:none" height="23px">
          <xsl:value-of select="''"/>
        </td>
        <td style="border-bottom:none" height="23px">
          <xsl:value-of select="''"/>
        </td>
        <td style="border-bottom:none" height="23px">
          <xsl:value-of select="''"/>
        </td>
        <!--        <td style="border-bottom:none" height="23px">
          <xsl:value-of select="''"/>
        </td>
        <td style="border-bottom:none" height="23px">
          <xsl:value-of select="''"/>
        </td>
        <td style="border-bottom:none" height="23px">
          <xsl:value-of select="''"/>
        </td>
        <td style="border-bottom:none" height="23px">
          <xsl:value-of select="''"/>
        </td>-->


      </tr>
      <xsl:call-template name="addLine">
        <xsl:with-param name="count" select="$count - 1"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>

  <xsl:template name="main">
    <xsl:param name="pagesNeededfnc"/>
    <xsl:param name="itemCountfnc"/>
    <xsl:param name="itemNeeded"/>
    <xsl:for-each select="Products//Product">
      <xsl:choose>
        <!-- Vị trí dòng product đầu mỗi trang -->
        <xsl:when test="position() mod $itemNeeded = 1">
          <xsl:choose>
            <!-- Dòng product đầu tiên của trang đầu -->
            <xsl:when test="position() = 1">
              <xsl:text disable-output-escaping="yes">&lt;div class="pagecurrent" id="1"&gt;</xsl:text>
              <xsl:text disable-output-escaping="yes">&lt;div class="pagecurrent2" &gt;</xsl:text>
              <xsl:call-template name="addfirtbody"> </xsl:call-template>
              <xsl:call-template name="addsecondbody"> </xsl:call-template>
              <xsl:text disable-output-escaping="yes">&lt;div class="statistics"&gt;</xsl:text>
              <xsl:text disable-output-escaping="yes">&lt;div class="nenhd"&gt;</xsl:text>
              <xsl:text disable-output-escaping="yes">&lt;table width="100%" class="dongcuoi" cellpadding="0" cellspacing="0" border="1" style="border-bottom: 0px solid ; margin-bottom:0px;"&gt;</xsl:text>
              <xsl:call-template name="calltitleproduct"> </xsl:call-template>
              <xsl:call-template name="callbodyproduct"> </xsl:call-template>
              <!-- Trường hợp chỉ có 1 sản phẩm product -->
              <xsl:if test="(position() = 1) and $itemCountfnc = 1">
                <xsl:call-template name="addLine">
                  <xsl:with-param name="count"
                    select="$pagesNeededfnc * $itemNeeded - $itemCountfnc"/>
                </xsl:call-template>
                <xsl:call-template name="callGuestIdInfo"> </xsl:call-template>
                <xsl:call-template name="calltongsoproduct"> </xsl:call-template>              
                <xsl:call-template name="callcheckinfo"> </xsl:call-template>
                <xsl:text disable-output-escaping="yes">&lt;/table&gt;</xsl:text>
                <xsl:text disable-output-escaping="yes">&lt;div class="nenhd_bg" style="margin-top: -252px; margin-left: 25px"&gt;&lt;/div&gt;</xsl:text>
                <xsl:text disable-output-escaping="yes">&lt;/div&gt;</xsl:text>
                <xsl:text disable-output-escaping="yes">&lt;/div&gt;</xsl:text>

                <xsl:call-template name="addchuky"> </xsl:call-template>
                <xsl:call-template name="addfinalbody"> </xsl:call-template>
                <xsl:text disable-output-escaping="yes">&lt;/div&gt;</xsl:text>
                <xsl:text disable-output-escaping="yes">&lt;/div&gt;</xsl:text>
                <xsl:text disable-output-escaping="yes">&lt;/div&gt;</xsl:text>
              </xsl:if>
            </xsl:when>
            <!-- Dòng product đầu của các trang sau -->
            <xsl:otherwise>
              <xsl:text disable-output-escaping="yes">&lt;div class="pagecurrent" id=</xsl:text>
              <xsl:value-of select="((position() - 1) div $itemNeeded) + 1"/>
              <xsl:text disable-output-escaping="yes">&gt;</xsl:text>

              <xsl:text disable-output-escaping="yes">&lt;div class="pagecurrent2" &gt;</xsl:text>
              <xsl:call-template name="addfirtbody"> </xsl:call-template>
              <div style="text-align:center; margin-top: 3px;font-family: Arial">
                <label>Tiếp theo trang trước - trang</label>
                <xsl:value-of select="((position() - 1) div $itemNeeded) + 1"/>/ <xsl:value-of
                  select="$pagesNeededfnc"/>
              </div>
              <xsl:call-template name="addsecondbody"> </xsl:call-template>
              <xsl:text disable-output-escaping="yes">&lt;div class="statistics"&gt;</xsl:text>
              <xsl:text disable-output-escaping="yes">&lt;div class="nenhd"&gt;</xsl:text>
              <xsl:text disable-output-escaping="yes">&lt;table width="100%" class="dongcuoi" cellpadding="0" cellspacing="0"  border="1" style="border-bottom: 0px solid"&gt;</xsl:text>
              <xsl:call-template name="calltitleproduct"> </xsl:call-template>
              <xsl:call-template name="callbodyproduct"> </xsl:call-template>
              <!-- Trường hợp dòng product cuối cùng là dòng đầu tiên của trang cuối cùng -->
              <xsl:if test="position() = $itemCountfnc">
                <xsl:call-template name="addLine">
                  <xsl:with-param name="count"
                    select="$pagesNeededfnc * $itemNeeded - $itemCountfnc"/>
                </xsl:call-template>
				 <xsl:call-template name="callGuestIdInfo"> </xsl:call-template>
                <xsl:call-template name="calltongsoproduct"> </xsl:call-template>
                <xsl:call-template name="callcheckinfo"> </xsl:call-template>
                <xsl:text disable-output-escaping="yes">&lt;/table&gt;</xsl:text>
                <xsl:text disable-output-escaping="yes">&lt;div class="nenhd_bg" style="margin-top: -252px; margin-left: 25px"&gt;&lt;/div&gt;</xsl:text>
                <xsl:text disable-output-escaping="yes">&lt;/div&gt;</xsl:text>

                <xsl:call-template name="addchuky"> </xsl:call-template>
                <xsl:call-template name="addfinalbody"> </xsl:call-template>
                <xsl:text disable-output-escaping="yes">&lt;/div&gt;</xsl:text>
                <xsl:text disable-output-escaping="yes">&lt;/div&gt;</xsl:text>
                <xsl:text disable-output-escaping="yes">&lt;/div&gt;</xsl:text>
              </xsl:if>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:when>
        <!-- Vị trí dòng product cuối cùng mỗi trang, không phải trang cuối -->
        <xsl:when test="(position() mod $itemNeeded = 0) and (position() &lt; $itemCountfnc)">
          <xsl:call-template name="callbodyproduct"> </xsl:call-template>
          <xsl:text disable-output-escaping="yes">&lt;/table&gt;</xsl:text>
          <xsl:text disable-output-escaping="yes">&lt;div class="nenhd_bg" style="margin-top: -252px; margin-left: 25px"&gt;&lt;/div&gt;</xsl:text>
          <xsl:text disable-output-escaping="yes">&lt;/div&gt;</xsl:text>
          <xsl:text disable-output-escaping="yes">&lt;/div&gt;</xsl:text>
          <xsl:text disable-output-escaping="yes">&lt;/div&gt;</xsl:text>
          <p style="page-break-before: always"/>
          <xsl:text disable-output-escaping="yes">&lt;/div&gt;</xsl:text>

        </xsl:when>
        <!-- Vị trí dòng sản phẩm cuối cùng -->
        <xsl:when test="position() = $itemCountfnc">
          <xsl:call-template name="callbodyproduct"> </xsl:call-template>
          <xsl:call-template name="addLine">
            <xsl:with-param name="count" select="$pagesNeededfnc * $itemNeeded - $itemCountfnc"/>
          </xsl:call-template>
		   <xsl:call-template name="callGuestIdInfo"> </xsl:call-template>
          <xsl:call-template name="calltongsoproduct"> </xsl:call-template>
          <xsl:call-template name="callcheckinfo"> </xsl:call-template>
          <xsl:text disable-output-escaping="yes">&lt;/table&gt;</xsl:text>
          <xsl:text disable-output-escaping="yes">&lt;div class="nenhd_bg" style="margin-top: -252px; margin-left: 25px"&gt;&lt;/div&gt;</xsl:text>
          <xsl:text disable-output-escaping="yes">&lt;/div&gt;</xsl:text>
          <xsl:text disable-output-escaping="yes">&lt;/div&gt;</xsl:text>
          <xsl:call-template name="addchuky"> </xsl:call-template>
          <xsl:call-template name="addfinalbody"> </xsl:call-template>
          <xsl:text disable-output-escaping="yes">&lt;/div&gt;</xsl:text>
          <xsl:text disable-output-escaping="yes">&lt;/div&gt;</xsl:text>
          <xsl:text disable-output-escaping="yes">&lt;/div&gt;</xsl:text>
        </xsl:when>
        <!-- Các vị trí dòng sản phẩm ở khoảng giữa một trang -->
        <xsl:otherwise>
          <xsl:call-template name="callbodyproduct"> </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:for-each>
  </xsl:template>
  <xsl:template name="addfirtbody">
    <table width="100%" style="margin-top: 5px;">
      <tr>
        <td colspan="3" style="text-align: center;">
          <table width="100%">
            <tr>
              <td>
                <div id="logo"/>
              </td>
            </tr>
            <tr>
              <td>
                <p class="" style="text-align: center;font-size: 24px;padding:5px 0">
                  <b>
                    <!--                          <xsl:value-of select="../../ComName"/>--> HOTEL
                    METROPOLE HANOI </b>
                </p>
              </td>
            </tr>
          </table>





          <!--          <table class="" cellpadding="0" cellspacing="0" border="0"
            style="text-align:center;margin-bottom: 0px;">
            <tbody>
              <tr>
                <td>
                  
                </td>
              </tr>
              <tr>
                <td>
                  <div id="logo"/>
                </td>
              </tr>
              
              
            </tbody>
          </table>-->
        </td>
      </tr>
      <tr>
        <td style="width:14%;text-align:left;vertical-align: top;">
          <table class="comname comname_bd" cellpadding="0" cellspacing="0" border="0"
            style="float: left; width:100%;margin-left: 0px; width: 330px; margin-bottom: 0px;">
            <tbody>
              <tr>
                <td>
                  <!--<div class="clsTable">

                    <div class="clsCol ">
                      <p class="" style="line-height: 20px;text-align: center;">
                        <b>
                          <xsl:value-of select="../../ComName"/>
                        </b>
                      </p>
                    </div>
                  </div>-->
                  <div class="clsTable">

                    <div class="clsCol col-txt">
                      <p class=""> 15 Ngô Quyền, Hà Nội, Việt Nam </p>
                    </div>
                  </div>
                  <div class="clsTable">
                    <div class="clsCol col-title">
                      <p style="font-family: Arial;"> Tel: (84 24) 3826 6919 </p>
                    </div>
                    <!--<div class="clsCol col-txt">
                      <p class=""> &#160; <b><xsl:value-of select="../../ComTaxCode"/></b>
                      </p>
                    </div>-->
                  </div>
                  <div class="clsTable">
                    <div class="clsCol col-title">
                      <p style="font-family: Arial;"> Fax: (84 24) 3826 6920 </p>
                    </div>
                    <!--<div class="clsCol col-txt">
                      <p class=""> &#160; <b><xsl:value-of select="../../ComTaxCode"/></b>
                      </p>
                    </div>-->
                  </div>
                  <!--<div class="clsTable">
                    <div class="clsCol col-title">
                      <p style="font-family: Arial;">
                        <b> Mã số thuế <i>/ VAT code</i>:</b>
                      </p>
                    </div>
                    <div class="clsCol col-txt">
                      <p class=""> &#160; <b><xsl:value-of select="../../ComTaxCode"/></b>
                      </p>
                    </div>
                  </div>-->
                  <!--	<div class="clsTable">
              <div class="clsCol col-title">
                <p style="font-family: Arial;">
                Điện thoại <i>(Tel)</i>:
                </p>
              </div>
              <div class="clsCol col-txt" >
                <p class="" >
                  &#160;<xsl:value-of select="../../ComPhone"/>
                </p> 
              </div>
            </div>

			  -->


                </td>
              </tr>

            </tbody>
          </table>
        </td>
        <td style="vertical-align: top;">
          <table border="0" style="width: 246px; float:right">
            <tbody>
              <tr>
                <td class="header">
                  <div class="header-note">
                    <!--<p style="font-size:14px;    color: #000 !important;">
                      <b> Mẫu số<!-\-<I>(Form No.)</I>-\->:&#160;</b>
                      <b>
                        <xsl:value-of select="../../InvoicePattern"/>
                      </b>
                    </p>-->
                    <!--<p
                      style="font-size:14px;    line-height: 35px;width: 100%;    color:#000 !important;"
                      > Ký hiệu<!-\- <i>(Serial)</i>-\->:&#160; <b>
                        <xsl:value-of select="../../SerialNo"/>
                      </b>
                    </p>-->
                    <p
                      style="font-size:14px;    line-height: 35px;width: 100%;    color:#000 !important;"
                      > Serie No<!-- <i>(Serial)</i>-->:&#160; <b> II/2018 </b>
                    </p>
                    <p
                      style="margin-top:0px;font-size:14px;    line-height: 15px;    color: #000 !important;">
                      <span style="font-size: 18px"> Số</span><!-- <i>(No)</i>-->:&#160; <xsl:choose>
                        <xsl:when test="../../CheckNo != ''">
                          <strong class="number" style="color: #ff0000 !important;font-size: 20px">
                            <xsl:call-template name="addZero">
                              <xsl:with-param name="count"
                                select="7 - string-length(../../CheckNo)"/>
                            </xsl:call-template>
                            <xsl:value-of select="../../CheckNo"/>
                          </strong>
                        </xsl:when>
                      </xsl:choose>
                    </p>
                    <!-- <p style="    font-size:14px;
    line-height: 31px;
    width: 100%;
    color:#000!important;">
                      Ngày HĐ <i>(Invoice Date)</i>:&#160;
                     <xsl:choose>
							<xsl:when test="substring(../../ArisingDate,7,4)!= '1957' and substring(../../ArisingDate,7,4)!= ''">
								<xsl:value-of select="../../ArisingDate"/>
							</xsl:when>
							<xsl:otherwise>
								&#160;&#160;&#160;
							</xsl:otherwise>
					</xsl:choose>
                    </p>-->

                  </div>

                </td>
              </tr>
            </tbody>
          </table>
        </td>
      </tr>
      <tr>
        <td style="text-align: center;padding-left: 0%;padding-right: 0%;" colspan="2">
          <!--<p class="name-upcase" style="color:#EB363A; font-size:20px; text-transform: uppercase;"><xsl:value-of select="../../InvoiceName"/></p>-->
          <p class="name-upcase"
            style="font-weight: bold;color:#000; margin-top: 0px;    line-height: 29px; margin-bottom: 0px;;font-size:18px; text-transform: uppercase;"
            > HÓA ĐƠN NỘI BỘ <br/>INTERNAL INVOICE</p>
       <!--   <p style="text-transform:uppercase;"><i>(Không có giá trị thanh toán / No commercial value)</i></p>-->

          <!-- <p style="color:#000000; font-size:14px;"> -->
          <!-- <i><xsl:value-of select="../../NoteINV"/></i> -->
          <!-- </p> -->
          <!--<p style="    font-size:14px;margin-top: 5px;">
										 <xsl:choose>
																					<xsl:when test="../../SignDate != ''">
                      Ngày&#160;<i>(Date)&#160;</i>
																						<label style=" color:#000">
																				
																								<xsl:value-of select="substring(../../SignDate,1,2)"/>
																					
																						</label>
                      Tháng&#160;<i>(Month)</i>&#160;
																						<label style=" color:#000">
																					
																								<xsl:value-of select="substring(../../SignDate,4,2)"/>
																						
																						</label>
                      Năm&#160;<i>(Year)</i>&#160;
																						<label style=" color:#000">
																						<xsl:value-of select="substring(../../SignDate,7,4)"/>
																							
																						</label>
																					</xsl:when>
																					<xsl:otherwise>
                      Ngày&#160;<i>(Date)&#160;</i><label style="border-bottom: 1px dotted #584C56;">&#160;&#160;&#160;&#160;</label> Tháng <i>(Month)</i> <label style="border-bottom: 1px dotted #584C56;">&#160;&#160;&#160;&#160;</label> năm <i>(Year)</i>&#160; <label style="border-bottom: 1px dotted #584C56;">&#160;&#160;&#160;&#160;</label>
																					</xsl:otherwise>
																				</xsl:choose>
									</p>
									-->
          <xsl:choose>
            <xsl:when test="../../../convert != ''">
              <b style="color:#000000; font-size:14px; margin-top: 7px;"> HÓA ĐƠN CHUYỂN ĐỔI TỪ HÓA
                ĐƠN ĐIỆN TỬ </b>
            </xsl:when>
          </xsl:choose>
        </td>

      </tr>
    </table>
  </xsl:template>
  <xsl:template name="addsecondbody">



    <!--    <table style="margin-top: 5px;width:65%   " class="cusname cusname_left">
      <!-\-<tr>
                <td width="100%">
                    <div class="clearfix" id="bt"></div>
                </td>
            </tr>-\->

      <tr>
        <td>
          <div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;"> Công ty <i>/ Company name</i>: </p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="    height: auto;"> &#160;<xsl:value-of
                  select="../../CusName"/>
              </p>
            </div>
          </div>


          <div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;"> Địa chỉ <i>/ Address</i>: </p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="">
                <xsl:value-of select="../../CusAddress"/>
                <br/>
              </p>
            </div>
          </div>
          <div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;"> Tên khách <i>/ Client name</i>: </p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="    "> &#160;<xsl:value-of select="../../CusName"/>
              </p>
            </div>
          </div>
          <div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;"> Mã số thuế <i>/ VAT code</i>: </p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="    height: auto;">
                <xsl:choose>
                  <xsl:when test="../../CusTaxCode != ''"> &#160;<xsl:value-of
                      select="../../CusTaxCode"/>
                  </xsl:when>
                  <xsl:otherwise> &#160; </xsl:otherwise>
                </xsl:choose>
              </p>
            </div>
          </div>
          <!-\-<div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;">
                Account No.:
              </p>
            </div>
            <div class="clsCol col-txt" >
              <p class="input-txt" style="    height: auto;">
                <xsl:choose>
                  <xsl:when test="../../PaymentMethod!=''">
                    &#160;<xsl:value-of select="../../PaymentMethod"/>
                  </xsl:when>
                  <xsl:otherwise>
                    &#160;
                  </xsl:otherwise>
                </xsl:choose>
              </p>
            </div>
          </div>-\->



        </td>
      </tr>
    </table>
    <table style="margin-top: 5px;  width:35%  " class="cusname cusname_right">
      <tr>
        <td style="padding-left: 0;">

          <div class="clsTable">

            <div class="clsCol col-title">
              <p style="font-family: Arial;">Ngày (Date)<span>:</span>
              </p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="text-align:left">
                <xsl:choose>
                  <xsl:when
                    test="substring(../../ArisingDate, 7, 4) != '1957' and substring(../../ArisingDate, 7, 4) != ''">
                    <xsl:value-of select="../../ArisingDate"/>
                  </xsl:when>
                  <xsl:otherwise> &#160;&#160;&#160; </xsl:otherwise>
                </xsl:choose>
              </p>
            </div>
          </div>







          <!-\- <div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;">
                Số tham chiếu <i> (Folio No.)</i>:
              </p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="text-align:left">
                <xsl:choose>
                  <xsl:when test="../../FolioNo!=''">
                    &#160;<xsl:value-of select="../../FolioNo"/>
                  </xsl:when>
                  <xsl:otherwise>
                    &#160;
                  </xsl:otherwise>
                </xsl:choose>
              </p>
            </div>
          </div>
           -\->


          <div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;"> Ngày đến (Arrival Date)<span>:</span>
              </p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="text-align:left">
                <xsl:choose>
                  <xsl:when test="string-length(../../Arrival) = 10"> &#160;<xsl:value-of
                      select="substring(../../Arrival, 1, 2)"/>.<xsl:value-of
                      select="substring(../../Arrival, 4, 2)"/>.<xsl:value-of
                      select="substring(../../Arrival, 9, 2)"/>
                  </xsl:when>
                  <xsl:when test="string-length(../../Arrival) = 8"> &#160;<xsl:value-of
                      select="substring(../../Arrival, 1, 2)"/>.<xsl:value-of
                      select="substring(../../Arrival, 4, 2)"/>.<xsl:value-of
                      select="substring(../../Arrival, 7, 2)"/>
                  </xsl:when>
                  <xsl:otherwise> &#160; </xsl:otherwise>
                </xsl:choose>
              </p>
            </div>
          </div>

          <div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;"> Ngày đi (Departure Date)<span>:</span>
              </p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="text-align:left">
                <xsl:choose>
                  <xsl:when test="string-length(../../Departure) = 10"> &#160;<xsl:value-of
                      select="substring(../../Departure, 1, 2)"/>.<xsl:value-of
                      select="substring(../../Departure, 4, 2)"/>.<xsl:value-of
                      select="substring(../../Departure, 9, 2)"/>
                  </xsl:when>
                  <xsl:when test="string-length(../../Departure) = 8"> &#160;<xsl:value-of
                      select="substring(../../Departure, 1, 2)"/>.<xsl:value-of
                      select="substring(../../Departure, 4, 2)"/>.<xsl:value-of
                      select="substring(../../Departure, 7, 2)"/>
                  </xsl:when>

                  <xsl:otherwise> &#160; </xsl:otherwise>
                </xsl:choose>
              </p>
            </div>
          </div>


          <div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;"> Số phòng (Room No)<span>:</span>
              </p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="text-align:left">
                <xsl:choose>
                  <xsl:when test="../../RoomNo != ''"> &#160;<xsl:value-of select="../../RoomNo"/>
                  </xsl:when>
                  <xsl:otherwise> &#160; </xsl:otherwise>
                </xsl:choose>
              </p>
            </div>
          </div>


          <div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;"> Số trang (Page)<span>:</span>
              </p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="text-align:left"> </p>
            </div>
          </div>

        </td>
      </tr>
    </table>
-->

    <table style="margin-top: 5px;  width:35%  " class="cusname cusname_right">
      <tr>
        <td style="padding-left: 0;">         
          
          <div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;"> Check</p><p class="delimiter">:</p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="    height: auto;">
                <xsl:choose>
                  <xsl:when test="../../CheckNo != ''"> &#160;<xsl:value-of select="../../CheckNo"/>
                  </xsl:when>
                  <xsl:otherwise> &#160; </xsl:otherwise>
                </xsl:choose>
              </p>
            </div>
          </div>
          
          <div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;"> Table No.</p><p class="delimiter">:</p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="    height: auto;">
                <xsl:choose>
                  <xsl:when test="../../TableNo != ''"> &#160;<xsl:value-of select="../../TableNo"/>
                  </xsl:when>
                  <xsl:otherwise> &#160; </xsl:otherwise>
                </xsl:choose>
              </p>
            </div>
          </div>
          
          <div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;"> Outlet</p><p class="delimiter">:</p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="    height: auto;">
                <xsl:choose>
                  <xsl:when test="../../Extra != ''"> &#160;<xsl:value-of select="../../Extra"/>
                  </xsl:when>
                  <xsl:otherwise> &#160; </xsl:otherwise>
                </xsl:choose>
              </p>
            </div>
          </div>

          <!--<div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;">&#160;</p><p class="delimiter">&#160;</p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="    height: auto;">
                <xsl:choose>
                  <xsl:when test="../../TableNo != ''"> &#160;<xsl:value-of select="../../TableNo"/>
                  </xsl:when>
                  <xsl:otherwise> &#160; </xsl:otherwise>
                </xsl:choose>
              </p>
            </div>
          </div>-->

          <div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;"> No. Guest</p><p class="delimiter">:</p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="    height: auto;">
                <xsl:choose>
                  <xsl:when test="../../Extra2 != ''"> &#160;<xsl:value-of
                      select="../../Extra2"/>
                  </xsl:when>
                  <xsl:otherwise> &#160; </xsl:otherwise>
                </xsl:choose>
              </p>
            </div>
          </div>
          <div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;">Open check</p><p class="delimiter">:</p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="text-align:left">
                <!--<xsl:choose>
                  <xsl:when test="string-length(../../ArisingDate) = 10"> &#160;<xsl:value-of
                    select="substring(../../ArisingDate, 1, 2)"/>.<xsl:value-of
                      select="substring(../../ArisingDate, 4, 2)"/>.<xsl:value-of
                        select="substring(../../ArisingDate, 9, 2)"/>
                  </xsl:when>
                  <xsl:when test="string-length(../../ArisingDate) = 8"> &#160;<xsl:value-of
                    select="substring(../../ArisingDate, 1, 2)"/>.<xsl:value-of
                      select="substring(../../ArisingDate, 4, 2)"/>.<xsl:value-of
                        select="substring(../../ArisingDate, 7, 2)"/>
                  </xsl:when>
                  <xsl:otherwise> &#160; </xsl:otherwise>
                </xsl:choose>-->
                <xsl:choose>
                  <xsl:when test="../../Departure != ''">&#160;<xsl:value-of select="../../Departure"/>
                  </xsl:when>
                  <xsl:otherwise> &#160; </xsl:otherwise>
                </xsl:choose>
              </p>
            </div>
          </div>
          <div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;">Close check</p><p class="delimiter">:</p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="text-align:left">
                <!--<xsl:choose>
                  <xsl:when test="string-length(../../ArisingDate) = 10"> &#160;<xsl:value-of
                    select="substring(../../ArisingDate, 1, 2)"/>.<xsl:value-of
                      select="substring(../../ArisingDate, 4, 2)"/>.<xsl:value-of
                        select="substring(../../ArisingDate, 9, 2)"/>
                  </xsl:when>
                  <xsl:when test="string-length(../../ArisingDate) = 8"> &#160;<xsl:value-of
                    select="substring(../../ArisingDate, 1, 2)"/>.<xsl:value-of
                      select="substring(../../ArisingDate, 4, 2)"/>.<xsl:value-of
                        select="substring(../../ArisingDate, 7, 2)"/>
                  </xsl:when>
                  <xsl:otherwise> &#160; </xsl:otherwise>
                </xsl:choose>-->
                <xsl:choose>
                  <xsl:when test="../../Arrival != ''">&#160;-&#160;<xsl:value-of select="../../Arrival"/>
                  </xsl:when>
                  <xsl:otherwise> &#160; </xsl:otherwise>
                </xsl:choose>
              </p>
            </div>
          </div>
        </td>
      </tr>
    </table>

  </xsl:template>
  <xsl:template name="calltitleproduct">
    <tr height="23px" style="    border-top: #000 1px solid;     ">
      <!-- <th width="25px" class="h1"> STT<br/>
        <i>(No.)</i>
      </th>-->
      <!-- <th width="55px" class="h1"> Ngày<br/>
        <i>(Date)</i>
      </th>-->

      <th width="25px" class="h1"> UNIT </th>
      <th width="160px" class="h1"> DESCRIPTION </th>
      <th width="80px" class="h1"> AMOUNT (VNĐ) </th>
    </tr>


  </xsl:template>
  <xsl:template name="callbodyproduct">
    <xsl:variable name="isContains">
      <xsl:call-template name="arrayContains">
        <xsl:with-param name="arr" select="$PaymentMethods"/>
        <xsl:with-param name="str" select="ProdName"/>
      </xsl:call-template>
    </xsl:variable>
    <tr class="noline back" style="    border-bottom: #36426F dotted 0px ">
      <!--  <td class="stt back-bold2" height="23px" style="text-align: center;">
        <xsl:value-of select="position()"/>
      </td>-->
      <!--      <td class="stt back-bold2" height="23px" style="text-align: center;">

        <xsl:choose>
          <xsl:when test="string-length(Extra1) = 10">
            <xsl:value-of select="substring(Extra1, 1, 2)"/>.<xsl:value-of
              select="substring(Extra1, 4, 2)"/>.<xsl:value-of select="substring(Extra1, 9, 2)"/>
          </xsl:when>
          <xsl:when test="string-length(Extra1) = 8">
            <xsl:value-of select="substring(Extra1, 1, 2)"/>.<xsl:value-of
              select="substring(Extra1, 4, 2)"/>.<xsl:value-of select="substring(Extra1, 7, 2)"/>
          </xsl:when>
          <xsl:otherwise> &#160; </xsl:otherwise>
        </xsl:choose>
      </td>-->
      <td class="back-bold2 text-center" style="text-align:center" height="23px">
        
		<xsl:value-of select="ProdQuantity"/>
      </td>
      <!--  <td class="back-bold2 text-center" style="text-align:center" height="23px">
        <xsl:value-of select="ProdUnit"/>
      </td>-->
      <td class="back-bold2" height="23px">
        <xsl:value-of select="ProdName"/>
      </td>

      <!--<td class="back-bold2" height="23px" style="text-align:right">
        <xsl:choose>
          <xsl:when test="ProdPrice = ''">
            <xsl:value-of select="''"/>
          </xsl:when>
          <xsl:when test="(ProdPrice = 0) or (not(ProdPrice >= 0))"> &#160; </xsl:when>

          <xsl:otherwise>
            <!-\- <xsl:value-of select="translate(translate(translate(format-number(ProdPrice, '###,###'),',','?'),'.',','),'?','.')"/> -\->
            <xsl:value-of
              select="translate(translate(translate(format-number(ProdPrice, '###,###'), ',', '?'), '.', ','), '?', '.')"
            />
          </xsl:otherwise>
        </xsl:choose>
      </td>-->
      <xsl:choose>
        <xsl:when test="(Amount = 0) or (Amount = '')">
          <td class="back-bold2" height="23px" style="text-align:right">
            <xsl:value-of select="Amount"/>
          </td>
        </xsl:when>
        <xsl:when
          test="not(Amount >= 0) or ($isContains = 'true')">
          <!--Tiền âm hoặc là hình thức thanh toán-->
          <td class="back-bold2" height="23px" style="text-align:right">
            <xsl:choose>
              <xsl:when test="(Amount > 0)">
                <!--Nếu tiền dương rồi thì khỏi chuyển-->
                <xsl:value-of
                  select="translate(translate(translate(format-number(Amount, '###,###'), ',', '?'), '.', ','), '?', '.')"
                />
              </xsl:when>
              <xsl:otherwise>
                <!--Chuyển âm sang dương-->
                <xsl:value-of
                  select="translate(translate(translate(format-number(Amount * -1, '###,###'), ',', '?'), '.', ','), '?', '.')"
                />
              </xsl:otherwise>
            </xsl:choose>
          </td>
        </xsl:when>
        <xsl:otherwise>
          <td class="back-bold2" height="23px" style="text-align:right">
            <xsl:value-of
              select="translate(translate(translate(format-number(Amount, '###,###'), ',', '?'), '.', ','), '?', '.')"
            />
          </td>
        </xsl:otherwise>
      </xsl:choose>
    </tr>
  </xsl:template>
  
  <xsl:template name="callGuestIdInfo">
    <xsl:variable name="items">
      <xsl:call-template name="splitStringToItems">
        <xsl:with-param name="delimiter" select="';'" />
        <xsl:with-param name="list" select="../../CusCountry" />
      </xsl:call-template>
    </xsl:variable>
    <table class="guestIdTable">
      <!--OpenCheck || ';' || FoodTotal || ';' || BevTotal || ';' || Cigarette || ';' || Others || ';' || Consigment || ';' || MHFacial || ';' || SpaTreatment || ';' || SpaMassage-->
      <tr class="noline back" style="border-top:  0px #000 solid; border-bottom: 0px;">
        <td style="border-right:none; border-left:none ;text-align:right; padding-right: 5px; vertical-align:top">    
          <div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;"> Food:</p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="text-align:right">
                <!-- <xsl:value-of select="msxsl:node-set($items)/*[2]"/> -->


<xsl:choose>
                <xsl:when test="../../CusCountry = 0"> 0 </xsl:when>
                <xsl:when test="../../CusCountry != ''">
                  <xsl:value-of
                    select="translate(translate(translate(format-number(../../CusCountry, '###,###'), ',', '?'), '.', ','), '?', '.')"
                  />
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="''"/>
                </xsl:otherwise>
              </xsl:choose>

              </p>
            </div>
          </div>
          
          <div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;"> Beverage:</p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="text-align:right">
                <!-- <xsl:value-of select="msxsl:node-set($items)/*[3]"/> -->

<xsl:choose>
                <xsl:when test="../../ARNumber = 0"> 0 </xsl:when>
                <xsl:when test="../../ARNumber != ''">
                  <xsl:value-of
                    select="translate(translate(translate(format-number(../../ARNumber, '###,###'), ',', '?'), '.', ','), '?', '.')"
                  />
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="''"/>
                </xsl:otherwise>
              </xsl:choose>

              </p>
            </div>
          </div>
          
          <div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;"> Non Alcohol:</p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="text-align:right">
                <!-- <xsl:value-of select="msxsl:node-set($items)/*[4]"/> -->
<xsl:choose>
                <xsl:when test="../../QuestQuantity = 0"> 0 </xsl:when>
                <xsl:when test="../../QuestQuantity != ''">
                  <xsl:value-of
                    select="translate(translate(translate(format-number(../../QuestQuantity, '###,###'), ',', '?'), '.', ','), '?', '.')"
                  />
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="''"/>
                </xsl:otherwise>
              </xsl:choose>

              </p>
            </div>
          </div>
          
          <div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;"> Misc:</p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="text-align:right">
                <!-- <xsl:value-of select="msxsl:node-set($items)/*[5]"/> -->


<xsl:choose>
                <xsl:when test="../../RoomNo = 0"> 0 </xsl:when>
                <xsl:when test="../../RoomNo != ''">
                  <xsl:value-of
                    select="translate(translate(translate(format-number(../../RoomNo, '###,###'), ',', '?'), '.', ','), '?', '.')"
                  />
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="''"/>
                </xsl:otherwise>
              </xsl:choose>

              </p>
            </div>
          </div>
          <div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;"> Discounts:</p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="text-align:right">

<xsl:choose>
                <xsl:when test="../../GuestID = 0"> 0 </xsl:when>
                <xsl:when test="../../GuestID != ''">
                  <xsl:value-of
                    select="translate(translate(translate(format-number(../../GuestID, '###,###'), ',', '?'), '.', ','), '?', '.')"
                  />
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="''"/>
                </xsl:otherwise>
              </xsl:choose>

              </p>
            </div>
          </div>
        </td>
        <td style="border-right:none; border-left:none ;text-align:right; padding-right: 5px; vertical-align:top">    
          <!--<div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;"> Discount Food:</p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="text-align:right">
               <!-\- <xsl:value-of select="msxsl:node-set($items)/*[1]"/>-\->
              </p>
            </div>
          </div>-->
          <!--<div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;"> Discount Bev:</p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="text-align:right">
<!-\-                <xsl:value-of select="msxsl:node-set($items)/*[2]"/>-\->
              </p>
            </div>
          </div>-->
          <!--<div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;"> Discounts:</p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="text-align:right">
                
<xsl:value-of
              select="translate(translate(translate(format-number(../../GuestID, '###,###'), ',', '?'), '.', ','), '?', '.')"
            />
<xsl:choose>
                <xsl:when test="../../Amount = 0"> 0 </xsl:when>
                <xsl:when test="../../Amount != ''">
                  <xsl:value-of
                    select="translate(translate(translate(format-number(../../Amount, '###,###'), ',', '?'), '.', ','), '?', '.')"
                  />
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="''"/>
                </xsl:otherwise>
              </xsl:choose>
              </p>
            </div>
          </div>-->
        </td>
      </tr>
    </table>
  </xsl:template>
  
  <xsl:template name="calltongsoproduct">
    <tfoot class="amount_table">
      <tr class="noline back" style="border-top:  0px #000 solid; border-bottom: 0px;">
        
        <td colspan="3"
          style="border-right:none ;border-left:none ;text-align:right;    padding-right: 5px;">
          
          <div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;"> Cộng tiền hàng <i>/ Sub Total</i>
              </p><p class="delimiter">:</p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="text-align:right">
                <xsl:choose>
                  <xsl:when test="../../Total = ''"> &#160;&#160; </xsl:when>
                  <xsl:when test="../../Total = 0">
                    <xsl:value-of select="../../Total"/>
                  </xsl:when>
                  <xsl:otherwise>
                    <!-- <xsl:value-of select="translate(translate(translate(format-number(../../Amount, '###,###'),',','?'),'.',','),'?','.')"/> -->
                    <xsl:value-of
                      select="translate(translate(translate(format-number(../../Total, '###,###'), ',', '?'), '.', ','), '?', '.')"
                    />
                  </xsl:otherwise>
                </xsl:choose>
              </p>
            </div>
          </div>
          
          
          
          <div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;"> Phí phục vụ <i>/ Service charge
                (5%)</i></p>
              <p class="delimiter">:</p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="text-align:right"> &#160;<xsl:choose>
                <xsl:when test="../../ServicechargeAmount = 0"> 0 </xsl:when>
                <xsl:when test="../../ServicechargeAmount != ''">
                  <xsl:value-of
                    select="translate(translate(translate(format-number(../../ServicechargeAmount, '###,###'), ',', '?'), '.', ','), '?', '.')"
                  />
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="''"/>
                </xsl:otherwise>
              </xsl:choose>
              </p>
            </div>
          </div>
          
          <div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;"> Thuế tiêu thụ đặc biệt <i>/ Special tax
                (30%)</i></p>
              <p class="delimiter">:</p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="text-align:right"> &#160;<xsl:choose>
                <xsl:when test="../../VatAmount30 = 0"> 0 </xsl:when>
                <xsl:when test="../../VatAmount30 != ''">
                  <xsl:value-of
                    select="translate(translate(translate(format-number(../../VatAmount30, '###,###'), ',', '?'), '.', ','), '?', '.')"
                  />
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="''"/>
                </xsl:otherwise>
              </xsl:choose>
              </p>
            </div>
          </div>
          <div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial"> Thuế suất GTGT <i>/ VAT rate
                (%)</i></p>
              <p class="delimiter">:</p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="color: rgb(7, 74, 142); text-align: left">&#160;
                <xsl:choose>
                  <xsl:when test="../../VATRate = -1"> / </xsl:when>
                  <xsl:when test="../../VATRate = 0"> 0% </xsl:when>
                  <xsl:when test="../../VATRate != ''">
                    <xsl:value-of select="../../VATRate"/>% </xsl:when>
                  <xsl:otherwise>
                    <xsl:value-of select="''"/>% </xsl:otherwise>
                </xsl:choose>
              </p>
            </div>
          </div>
          <div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;"> Tiền thuế GTGT <i>/ VAT
                Amount</i>
              </p><p class="delimiter">:</p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="text-align:right; ">
                <xsl:choose>
                  <xsl:when test="../../VATRate = -1"> / </xsl:when>
                  <xsl:when test="../../VATAmount = 0"> &#160;0 </xsl:when>
                  <xsl:when test="../../VATAmount != ''">
                    <xsl:value-of
                      select="translate(translate(translate(format-number(../../VATAmount, '###,###'), ',', '?'), '.', ','), '?', '.')"/>
                    
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:value-of select="''"/>
                  </xsl:otherwise>
                </xsl:choose>
              </p>
            </div>
          </div>
          
          
          <div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;"> Tổng cộng tiền thanh toán &#160;<i>/ Total
                Amount</i>
              </p><p class="delimiter">:</p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt" style="text-align:right"> &#160;<xsl:choose>
                <xsl:when test="../../Amount = 0"> 0 </xsl:when>
                <xsl:when test="../../Amount != ''">
                  <xsl:value-of
                    select="translate(translate(translate(format-number(../../Amount, '###,###'), ',', '?'), '.', ','), '?', '.')"
                  />
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="''"/>
                </xsl:otherwise>
              </xsl:choose>
              </p>
            </div>
          </div>
          
          
          <div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;"> Số tiền bằng chữ <i>/ Amount in
                words</i>
              </p><p class="delimiter">:</p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt"
                style=" text-align:left;width:auto;    line-height: 16px;    border-bottom: 0px dotted rgba(0, 0, 0, 0.5);"
                > &#160;<xsl:choose>
                  <xsl:when test="../../AmountInWords != ''">
                    <xsl:value-of select="../../AmountInWords"/>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:value-of select="''"/>
                  </xsl:otherwise>
                </xsl:choose>
              </p>
            </div>
          </div>
          <div class="clsTable">
            <div class="clsCol col-title">
              <p style="font-family: Arial;"> Amount in words <i>/ Amount in
                words english</i>
              </p><p class="delimiter">:</p>
            </div>
            <div class="clsCol col-txt">
              <p class="input-txt"
                  style=" text-align:left;width:auto;    line-height: 16px;    border-bottom: 0px dotted rgba(0, 0, 0, 0.5);"
              > &#160;<xsl:choose>
                <xsl:when test="../../AmountInWordsEnglish != ''">
                  <xsl:value-of select="../../AmountInWordsEnglish"/>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="''"/>
                </xsl:otherwise>
              </xsl:choose>
              </p>
            </div>
          </div>
        </td>
      </tr>
      
      
      
      
      
      
      
    </tfoot>
  </xsl:template>
  <xsl:template name="callcheckinfo">
    <table class="checkinfo">
     
<tr>
        <td>
          <div class="clsCol col-title">
            <p style="font-family: Arial;"> Cashier Name<span>:</span> </p>
          </div>
        </td>
        <td>
          <div class="clsCol col-txt">
            <p class="input-txt"
              style=" text-align:left;    line-height: 16px;    border-bottom: 0px dotted rgba(0, 0, 0, 0.5);"
              > &#160;<xsl:choose>
                <xsl:when test="../../Cashier != ''">
                  <xsl:value-of select="../../Cashier"/>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="''"/>
                </xsl:otherwise>
              </xsl:choose>
            </p>
          </div>
        </td>
      </tr>
      <tr>
        <td width="200">
          <div class="clsCol col-title">
            <p style="font-family: Arial;"> Room No: </p>
          </div>
        </td>
        <td>
          <div class="clsCol col-txt">
            <p class="input-txt"
              style=" text-align:left;    line-height: 16px;    border-bottom: 0px dotted rgba(0, 0, 0, 0.5);"
              > &#160;<xsl:choose>
                <xsl:when test="../../Rate != ''">
                  <xsl:value-of select="../../Rate"/>
                </xsl:when>
                <xsl:when test="../../RoomNo != ''">
                    <xsl:value-of select="../../RoomNo"/>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="''"/>
                </xsl:otherwise>
              </xsl:choose>
            </p>
          </div>
        </td>
      </tr>
      <tr>
        <td>
          <div class="clsCol col-title">
            <p style="font-family: Arial;"> Guest's Name<span>:</span></p>
          </div>
        </td>
        <td>
          <div class="clsCol col-txt">
            <p class="input-txt"
              style=" text-align:left;    line-height: 16px;    border-bottom: 0px dotted rgba(0, 0, 0, 0.5);"
              > &#160;<xsl:choose>
                <xsl:when test="../../CusName != ''">
                  <xsl:value-of select="../../CusName"/>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="''"/>
                </xsl:otherwise>
              </xsl:choose>
            </p>
          </div>
        </td>
      </tr>
      <tr>
        <td>
          <div class="clsCol col-title">
            <p style="font-family: Arial;"> Guest's Signature: </p>
          </div>
        </td>
        <td>
          <div class="clsCol col-txt">
            <p class="input-txt"
              style=" text-align:left;    line-height: 16px;    border-bottom: 0px dotted rgba(0, 0, 0, 0.5);"
              > &#160;<xsl:choose>
                <xsl:when test="../../RoomNo != ''">
                 <xsl:value-of select="''"/>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="''"/>
                </xsl:otherwise>
              </xsl:choose>
            </p>
          </div>
        </td>
      </tr>
    </table>
  </xsl:template>
  <xsl:template name="addfinalbody">
    <div class="statistics"> </div>
  </xsl:template>
  <xsl:template name="addchuky">
    <div class="statistics">
      <table style="width:100%" cellspacing="0" cellpadding="0" border="0">
        <tr>
          <td style="padding: 0;">
            <table style="width:100%">
              <tbody>
                <!--panel footer-->
                <!--variable-->
                <xsl:variable name="serial">
                  <xsl:value-of select="../../SerialNo"/>
                </xsl:variable>
                <xsl:variable name="pattern">
                  <xsl:value-of select="../../InvoicePattern"/>
                </xsl:variable>
                <xsl:variable name="invno">
                  <xsl:value-of select="../../InvoiceNo"/>
                </xsl:variable>
                <!---->

                <!--panel adjust-->
                <xsl:choose>
                  <xsl:when test="../../isReplace != ''">
                    <tr>
                      <td>
                        <div id="ReplaceInv"
                          style="text-align:center;padding-top:0px;font-size:14px;text-transform:uppercase">
                          <xsl:value-of select="../../isReplace"/>
                        </div>
                      </td>
                    </tr>
                  </xsl:when>
                  <xsl:when test="../../isAdjust != ''">
                    <tr>
                      <td>
                        <div id="AdjustInv"
                          style="text-align:center;padding-top:0px;font-size:14px;text-transform:uppercase">
                          <xsl:value-of select="../../isAdjust"/>
                        </div>
                      </td>
                    </tr>
                  </xsl:when>

                </xsl:choose>

                <tr>
                  <td style="padding-bottom: 0;">
                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                      <tr style="    vertical-align: top;">


                        <!-- <td width="30%"> -->
                         <!-- <div class="payment fl-l" style="width:100%;float:left; "> -->
                            <!-- <div style="text-align:center;"> -->
                              <!-- <div> -->
                                
                                <!-- <div style="    text-align: center;"> -->
                                  
                                  <!-- <p style="font-size:14px; margin:0px;   "> Chữ ký khách hàng<br/> -->
                                    <!-- <i>Client name</i></p> -->
                                 
					 <!-- <xsl:choose> -->
								  <!-- <xsl:when test="../../UserSign!=''"> -->
								  <!-- <div style="float: right; margin-right: 48px"> -->
									<!-- <p> -->
								   <!-- <img style="height:120px"> -->
									 <!-- <xsl:attribute name="src"> data:image/png;base64,<xsl:value-of select="../../UserSign"/></xsl:attribute> -->
								   <!-- </img> -->
									<!-- </p> -->
									<!-- </div> -->
								  <!-- </xsl:when> -->
								  <!-- <xsl:otherwise>       -->
								  <!-- </xsl:otherwise> -->
								   <!-- </xsl:choose>						  -->
                                <!-- </div> -->
                                
                              <!-- </div> -->
                            <!-- </div> -->
                          <!-- </div> -->
                        <!-- </td> -->
						<td width="30%">
                          <!-- <div class="payment fl-l" style="width:100%;float:left; ">
                            <div style="text-align:center;">
                              <div>
                                
                                <div style="    text-align: center;">
                                  
                                  <p style="font-size:14px; margin:0px;   "> Chữ ký khách hàng<br/>
                                    <i>Client name</i></p>
                                  
                                </div>
                                
                              </div>
                            </div>
                          </div>-->
						  <div class="payment fl-l" style="width:100%;float:left; ">
                            <div style="text-align:center;">
                              <div>
                                
                                <div style="  text-align: center;height:150px;">
                                  
                                  <p style="font-size:14px; margin:0px;   "> Chữ ký khách hàng<br/>
                                    <i>Client confirm</i></p>
                                  <xsl:choose>
                                    <xsl:when test="../../imageSignedCus != ''">
                                    <img style="height:120px;width:200px;">
                                      <xsl:attribute name="src">
                                        <xsl:value-of select="../../imageSignedCus"/>
                                      </xsl:attribute>
                                    </img>

                                    </xsl:when>
                                  </xsl:choose>
                                    <xsl:choose>
                                        <xsl:when test="../../UserSign!=''">

                                                    <img style="height:120px">
                                                        <xsl:attribute name="src"> data:image/png;base64,<xsl:value-of select="../../UserSign"/></xsl:attribute>
                                                    </img>

                                        </xsl:when>
                                        <xsl:otherwise>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </div>
                                
                              </div>
                            </div>
                          </div>
                        </td>
                        <td width="36%">
                          <div class="payment fl-l convert"
                            style="margin-top:0px;width: 100% !important;float: left;">
                            <xsl:choose>
                              <xsl:when test="/Invoice/convert != ''">
                                <xsl:choose>
                                  <xsl:when test="/Invoice/ConvertDate != ''">
                                    <div id="comname-ky">


                                      <div style="    text-align: center;">
                                        <p style="font-size:14px; margin:0px;font-family: Arial;  "
                                          >Người chuyển đổi<br/><i>Converter</i></p>

                                        <!-- <span style="font-size:14px;">Ký, ghi rõ họ tên</span><br/>
								  <i style="font-size:14px;">
										(Sign, <![CDATA[&]]> full name)
								  </i>-->
                                      </div>

                                      <p
                                        style="margin-top:50px !important; font-size: 14px; !important;">
                                        <xsl:choose>
                                          <xsl:when test="/Invoice/ConvertBy != ''">
                                            <xsl:value-of select="/Invoice/ConvertBy"/>
                                          </xsl:when>
                                          <xsl:otherwise> &#160;&#160; </xsl:otherwise>
                                        </xsl:choose>
                                      </p>
                                      <p style="font-size:14px; margin:0px;"> Ngày <xsl:value-of
                                          select="substring(/Invoice/ConvertDate, 1, 2)"/> tháng
                                          <xsl:value-of
                                          select="substring(/Invoice/ConvertDate, 4, 2)"/> năm
                                          <xsl:value-of
                                          select="concat('20', substring(/Invoice/ConvertDate, 9, 2))"
                                        />
                                      </p>
                                    </div>
                                  </xsl:when>
                                  <xsl:otherwise>
                                    <div id="comname-ky" style="    text-align: center;">

                                      <p style="font-size:14px; margin:0px;font-family: Arial;  "
                                        >Người chuyển đổi<br/><i>Converter</i></p>

                                      <p
                                        style="margin-top:80px !important; font-size: 14px; !important;"
                                        > Ngày......tháng......năm....... &#160;&#160; </p>
                                    </div>
                                  </xsl:otherwise>
                                </xsl:choose>
                              </xsl:when>
                            </xsl:choose>

                          </div>
                        </td>
                        <td width="34%">
                          <div class="payment fl-l" style="width:100%;float:left;  ">
                            <div style="text-align:center;">
                              <div>

                                <p style="font-size:14px; margin:0px;"> Người lập hóa đơn<br/>
                                  <i>Attendant</i><br/>
                                </p>
                                <!--   <span style="font-size:14px;">Ký, ghi rõ họ tên</span><br/>
								  <i style="font-size:14px;">
									(Sign, <![CDATA[&]]> full name)
								  </i>-->
                                <div class="date fl-r"
                                  style="margin-top:0px;float: left; margin-right:0px; width:100% !important">
                                  <p style="font-size:12px; margin:0px">
                                    <xsl:choose>
                                      <xsl:when test="/Invoice/image != ''">
                                        <div class="bgimg"
                                          style="background:url(data:image/png;base64,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) no-repeat center center; height: auto">
                                          <p
                                            style="margin-top:3px;margin-bottom:5px;  color: #FF2107 !important;">
                                            <xsl:value-of select="/Invoice/image"/>
                                          </p>
                                          <p style="  color: #FF2107 !important; font-size:14px"> Ký
                                            bởi: <xsl:value-of select="../../ComName"/>
                                            <!-- <br/> Ký ngày: <xsl:value-of select="../../SignDate"/> -->
                                          </p>
                                        </div>
                                      </xsl:when>
                                    </xsl:choose>
                                  </p>
                                </div>
                              </div>
                            </div>
                          </div>
                        </td>
                      </tr>
                      <tr>
                        <td width="100%" colspan="3">
                          <div class="nenhd_bg2">&#160;&#160;&#160; </div>
                        </td>
                      </tr>
                      <!--  <tr>
                        <td width="30%">

                          <div class="clsTable">
                            <div class="clsCol col-title">
                              <p style="font-family: Arial;"> So Folio No </p>
                            </div>
                            <div class="clsCol col-txt">
                              <p class="input-txt"
                                style=" text-align:left;    line-height: 16px;    border-bottom: 0px dotted rgba(0, 0, 0, 0.5);"
                                > &#160;<xsl:choose>
                                  <xsl:when test="../../FolioNo != ''">
                                    <xsl:value-of select="../../FolioNo"/>
                                  </xsl:when>
                                  <xsl:otherwise>
                                    <xsl:value-of select="''"/>
                                  </xsl:otherwise>
                                </xsl:choose>
                              </p>
                            </div>
                          </div>
                        </td>
                        <td width="35%" style="position: relative;">

                          <div class="nenhd_bg2">&#160;&#160;&#160; </div>
                        </td>
                        <td width="30%"> </td>
                      </tr>-->

                    </table>
                  </td>
                </tr>




              </tbody>
            </table>
          </td>
        </tr>
      </table>
<!--      <div id="SolutionVNPT"
        style="margin-bottom:4px;    padding-bottom: 1px;font-family: 'Times New Roman';font-size:9px;border-top:1px dashed rgb(7, 74, 142);     width: 100%;color:#000; text-align: center; background-color: rgba(255, 255, 255, 0);;">
        <label style="font-family: 'Times New Roman';font-size: 13px;"> Đơn vị cung cấp giải pháp
          hóa đơn điện tử: Tổng công ty dịch vụ viễn thông - VNPT Vinaphone, MST:0106869738, Điện
          thoại:18001260 </label>
      </div>-->
    </div>
    <!-- <xsl:choose>
		<xsl:when test="../../../convert!=''">
	                <div class="clearfix">
					    <label class="fl-l">Tra cứu hóa đơn chuyển đổi tại website:</label>
						<label class="fl-l input-name" style="width:277px; height:15px"><xsl:value-of select="''"/></label>
					</div>
		</xsl:when>
	</xsl:choose> -->
  </xsl:template>
  <xsl:template match="/">
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
        <!--<link href="styles.css" type="text/css" rel="stylesheet" />-->
        <title>VAT</title>
        <div style="display:none">
          <![CDATA[
if (lt IE 9)
<script src="http://ie7-js.googlecode.com/svn/version/2.1(beta4)/IE9.js"></script>
]]>
        </div>
        <style type="text/css" rel="stylesheet">
          @charset utf-8;
          * html,
          body {
          margin: 0;
          padding: 0;
       <!--   font-family: Arial;
          font-size:14px;-->
          background-color: rgba(255, 255, 255, 0);
          }

          #main {
          margin: 0 auto
          }
		.modal-footer{    border-top: 0px solid #e5e5e5 !important;}
          .VATTEMP {
          background-color: rgba(255, 255, 255, 0);;
          color: #000;
          font: 78%/1.4em Arial,Helvetica,sans-serif !important;
          margin: 0px auto;
          padding: 5px 0;
          <!--width: 870px;-->
          width: 600px;
          font-size:14px;
          position: relative;
          padding-left: 30px;
          }

          .footer_{
              background-image: url(data:image/png;base64,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AAAAAQAAAAAAAFJHQkMAAAADAAAAAFJkICBkb3ViQG/gAAAAAAAAAAAAR3JuIGRvdWJAb+AAAAAAAAAAAABCbCAgZG91YkBv4AAAAAAAAAAAAEJyZFRVbnRGI1JsdAAAAAAAAAAAAAAAAEJsZCBVbnRGI1JsdAAAAAAAAAAAAAAAAFJzbHRVbnRGI1B4bEBSAAAAAAAAAAAACnZlY3RvckRhdGFib29sAQAAAABQZ1BzZW51bQAAAABQZ1BzAAAAAFBnUEMAAAAATGVmdFVudEYjUmx0AAAAAAAAAAAAAAAAVG9wIFVudEYjUmx0AAAAAAAAAAAAAAAAU2NsIFVudEYjUHJjQFkAAAAAAAAAAAAQY3JvcFdoZW5QcmludGluZ2Jvb2wAAAAADmNyb3BSZWN0Qm90dG9tbG9uZwAAAAAAAAAMY3JvcFJlY3RMZWZ0bG9uZwAAAAAAAAANY3JvcFJlY3RSaWdodGxvbmcAAAAAAAAAC2Nyb3BSZWN0VG9wbG9uZwAAAAAAOEJJTQPtAAAAAAAQAEgAAAABAAEASAAAAAEAAThCSU0EJgAAAAAADgAAAAAAAAAAAAA/gAAAOEJJTQQNAAAAAAAEAAAAHjhCSU0EGQAAAAAABAAAAB44QklNA/MAAAAAAAkAAAAAAAAAAAEAOEJJTScQAAAAAAAKAAEAAAAAAAAAAThCSU0D9QAAAAAASAAvZmYAAQBsZmYABgAAAAAAAQAvZmYAAQChmZoABgAAAAAAAQAyAAAAAQBaAAAABgAAAAAAAQA1AAAAAQAtAAAABgAAAAAAAThCSU0D+AAAAAAAcAAA/////////////////////////////wPoAAAAAP////////////////////////////8D6AAAAAD/////////////////////////////A+gAAAAA/////////////////////////////wPoAAA4QklNBAgAAAAAABAAAAABAAACQAAAAkAAAAAAOEJJTQQeAAAAAAAEAAAAADhCSU0EGgAAAAADawAAAAYAAAAAAAAAAAAAAGoAAAECAAAAGwAxADMAOAA1ADUAMwA1ADkAMAAwADYAMAA2AC0AMQAzADgANQA0ADkANAA5ADUAMAAzADkANQAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAB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AGUAIABQAGgAbwB0AG8AcwBoAG8AcAAAABMAQQBkAG8AYgBlACAAUABoAG8AdABvAHMAaABvAHAAIABDAFMANgAAAAEAOEJJTQQGAAAAAAAHAAgAAAABAQD/4Q19aHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLwA8P3hwYWNrZXQgYmVnaW49Iu+7vyIgaWQ9Ilc1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCI/PiA8eDp4bXBtZXRhIHhtbG5zOng9ImFkb2JlOm5zOm1ldGEvIiB4OnhtcHRrPSJBZG9iZSBYTVAgQ29yZSA1LjMtYzAxMSA2Ni4xNDU2NjEsIDIwMTIvMDIvMDYtMTQ6NTY6MjcgICAgICAgICI+IDxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+IDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIiB4bWxuczpzdEV2dD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3NUeXBlL1Jlc291cmNlRXZlbnQjIiB4bWxuczpkYz0iaHR0cDovL3B1cmwub3JnL2RjL2VsZW1lbnRzLzEuMS8iIHhtbG5zOnBob3Rvc2hvcD0iaHR0cDovL25zLmFkb2JlLmNvbS9waG90b3Nob3AvMS4wLyIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bXBNTTpEb2N1bWVudElEPSI1QkNBRDcyNzM0QTE2OTdBODc4RTg2MTcxQTE3NjRCOCIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpFNjlFRDM3RUVDQzVFNzExQTU5NkJDMUQwRTRBQ0VFRSIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSI1QkNBRDcyNzM0QTE2OTdBODc4RTg2MTcxQTE3NjRCOCIgZGM6Zm9ybWF0PSJpbWFnZS9qcGVnIiBwaG90b3Nob3A6Q29sb3JNb2RlPSIzIiB4bXA6Q3JlYXRlRGF0ZT0iMjAxNy0xMS0xMFQxNDo1MzozNSswNzowMCIgeG1wOk1vZGlmeURhdGU9IjIwMTctMTEtMTBUMTU6MjY6NTcrMDc6MDAiIHhtcDpNZXRhZGF0YURhdGU9IjIwMTctMTEtMTBUMTU6MjY6NTcrMDc6MDAiPiA8eG1wTU06SGlzdG9yeT4gPHJkZjpTZXE+IDxyZGY6bGkgc3RFdnQ6YWN0aW9uPSJzYXZlZCIgc3RFdnQ6aW5zdGFuY2VJRD0ieG1wLmlpZDpFNTlFRDM3RUVDQzVFNzExQTU5NkJDMUQwRTRBQ0VFRSIgc3RFdnQ6d2hlbj0iMjAxNy0xMS0xMFQxNDo1NToxOCswNzowMCIgc3RFdnQ6c29mdHdhcmVBZ2VudD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoV2luZG93cykiIHN0RXZ0OmNoYW5nZWQ9Ii8iLz4gPHJkZjpsaSBzdEV2dDphY3Rpb249InNhdmVkIiBzdEV2dDppbnN0YW5jZUlEPSJ4bXAuaWlkOkU2OUVEMzdFRUNDNUU3MTFBNTk2QkMxRDBFNEFDRUVFIiBzdEV2dDp3aGVuPSIyMDE3LTExLTEwVDE1OjI2OjU3KzA3OjAwIiBzdEV2dDpzb2Z0d2FyZUFnZW50PSJBZG9iZSBQaG90b3Nob3AgQ1M2IChXaW5kb3dzKSIgc3RFdnQ6Y2hhbmdlZD0iLyIvPiA8L3JkZjpTZXE+IDwveG1wTU06SGlzdG9yeT4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPD94cGFja2V0IGVuZD0idyI/Pv/uAA5BZG9iZQBkQAAAAAH/2wCEAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEB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ti54v+P6e7VbQy6u+mD6emPOnVSpqKN+n5j1Hnnyr1wMDHTaUizBraFbkfkX5H+8296AUgeINRp9lT64/wAHVWRghWB9BLVJpWo/hz/h4465CG39q9iPx/jc3AIFyef6f4e2/CUoyMKqWqPl6eeadOkmihTQdd+L06QwHAH6brwOPQWItf8A2P8Aj7cTUpGpqqPy6qR/aFcMwOeOT5/7HWM07adImN9AXUUW+oC2qy6ANQJuFA+vFrD37SoaRgvc1fXz/Py63V/DRQ1JB50Gfy4dc/CbW1n6Wvbn/e/rbi/1908NSJA+dQI9MHqoHerHgMkep9f9jh15YdK6dV+QfpYXAtwNR493jURhVT4AtKfZ1tBoaVq11MT9leufj/x/Fvp/xv3TQ38f49XDy9P9nq+oenn1/9Tf49+691737r3Xvfuvde9+691737r3Xvfuvde9+691737r3Xvfuvde9+691737r3Xvfuvde9+691737r3Xvfuvde9+691737r3Xvfuvde9+691737r3Xvfuvde9+691737r3Xvfuvde9+691737r3Xvfuvde9+691737r3Xvfuvde9+691737r3Xvfuvdf/9k=);
    background-repeat: no-repeat;
    background-size: 102%;
    background-position: center 5%;
      width: 200px;
    float: right;
    height: 123px;
          }
          .VATTEMP .header {
          float: left;
          width:100%;
          }

          .VATTEMP .header-content {
          float: left;
          text-align: center;
          width: 400px
          }
          .VATTEMP .comname td b{
          font-size:16px;
          }

          .VATTEMP .header h2 {
          font-size: 1em
          }

          .VATTEMP .header h2,
          .header p {
          margin: 0
          }

          .VATTEMP .header p.name-upcase {
          font-size:14px;
          text-transform: uppercase
          }

          .VATTEMP .header-note {
          float:right;
          font-size:14px;
          <!--   width: 190px;-->
          margin-top: 2px;

          }

          .VATTEMP .header .number {

          font-family: Arial;
          font-size:14px;
          margin-left: 3px;
          }

          .clearfix:after {
          clear: both;
          content: ".";
          display: block;
          height: 1px;
          overflow: hidden;
          visibility: hidden
          }
			#logo{
			background-repeat: no-repeat;
			background-size: 100%;
			background-position: center 5%;
			width: 120px;
			height: 123px;
			margin:auto;	background-image:url(data:image/png;base64,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);
			}
          .clearfix {
          clear: both
          }
          .VATTEMP .cusname{
          width:100%;
          float:left;
          margin-bottom: 5px;
          }
          .inv_Footer img{float:right}
          .inv_Footer span{color:#000; float:left;font-size:14px;    line-height: 23px;margin-left: 12px;}
          .inv_Footer p{    color: #000;
          margin-right: 12px;
          float: right;
          letter-spacing: 1px;
          line-height: 24px;
          font-size: 8px;
          border-left: #000 solid thin;
          margin-top: 8px;
          margin-left: 4px;
          /* padding-top: 10px; */
          height: 15px;
          padding-left: 5px;}
          .VATTEMP .cusname td{
        padding: 0px;
          }
          .VATTEMP .input-code {
          border: 1px solid #000;
          color: #000;
          float: left;
          font-weight: normal;
          text-align: center;
          width: 18px;
          height: 15px
          }
          .comname_bd .clsCol p{
      
          }
          .VATTEMP .dongcuoi tr:last-child{
        <!--  border-bottom:#000 1px solid !important;-->
          }
          .VATTEMP div label.fl-l,
          div label {
              margin-right: 5px;
              margin-top: 0px
          }
          
          .VATTEMP .input-name,
          .input-date {
              border: 0;
              border-bottom: 1px dotted #000
          }
          
          .VATTEMP .statistics {
              clear: both;
              margin-right: 0;
              padding-top: 2px
          }
          
          .VATTEMP .guestIdTable td {
          float: left;
          }
          .VATTEMP .guestIdTable .col-title p {
          color: rgb(7, 74, 142) !important;
          text-align: left;         
          }
          
          .VATTEMP .guestIdTable .col-txt p {        
          text-align: right;
          }
          
          .VATTEMP .statistics .checkinfo {
              padding-top: 2px;
              margin: 10px 0;
          }
          
          .VATTEMP .statistics .checkinfo td {
              border: solid 1px;
              vertical-align: top;
          }
          
          .VATTEMP .statistics .checkinfo .clsCol p {
              padding-left: 5px;
          }
          
          .nenhd {
          
              position: relative
          }
          .nenhd_bg {
              width: 0;
              height: 0;
              top: 0px;
              left: 200px;
              margin: 0 auto;
              background-size: 93%;
              position: absolute;
              text-align: center;
              vertical-align: middle;
              z-index: 1;
          }
          .nenhd_bg2 {
              opacity: 1;
              background: url(data:image/png;base64,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) no-repeat scroll transparent;
              width: 150px;
              height: 50px;
              top: 0px;
              left: 200px;
              margin: 0 auto;
              background-size: 93%;
              /* position: absolute; */
              text-align: center;
              vertical-align: middle;
              z-index: 1;
          }
          .dongcuoi tfoot td p {
              font-size: 14px;
              color: #000;
          }
          .dongcuoi tfoot tr td:last-child p {
              color: rgb(7, 74, 142)
          }
          .VATTEMP .pagecurrent2 {
          
          
          
          }<!-- .dongcuoi tr td:first-child,.dongcuoi tr th:first-child{-->
          .dongcuoi tr th:first-child {
              border-left: #000 1px solid;
          }<!--    .dongcuoi tr td:last-child,.dongcuoi tr th:last-child{-->
      .dongcuoi tr th:last-child{
          border-right: #ccc 3px solid;
          }
          .comname .col-txt p{
          color:#000 !important;
          }
          .VATTEMP .statistics table {
          background-position: bottom;
          border: 0 none;
          font-size:14px;
          margin: 0 auto;
          position: relative;
          z-index: 2;
          width: 100%;
<!--          table-layout: fixed-->
          }

          table {
          border-collapse: collapse;
        <!--  font-family: Arial;
          font-size:14px-->
          }
          .VATTEMP table td, table th{
          word-wrap: break-word;
          }


          .VATTEMP .statistics table th.h1 {
              text-align: center;
          font-size:14px;
          font-weight:normal;
        <!--  normal-->
          text-transform: none;

          color: #000;
          padding: 2px;
        border-right: #000 1px solid;

          border-top: #000 1px solid;
          background-color: rgba(255, 255, 255, 0);
          border: none;
          }

          .VATTEMP .statistics table th.h2 {
          font-size:14px;
          text-transform: none;
          color: #000;
          font-weight: normal;
          border-bottom:  #000 solid thin;
          border-right: #000 1px solid;
          border-left: #000 1px solid;
          border-top: 1px solid #rgb(7, 74, 142);
          background-color: rgba(255, 255, 255, 0);
          }

          .VATTEMP .statistics table td.stt {
          text-align: center
          }

          .VATTEMP .statistics table td.stt2 {
          text-align: center;
          color: #000
          }

          .VATTEMP .statistics table .back td {
          color: #000;
          font-family: Arial;
          font-size:14px
          }

          .VATTEMP .statistics table .noline td {
          border-bottom: none;
          border-right: 0px solid rgb(7, 74, 142);
    border-left: #000 0px solid;
          border-top: none
          }
          .VATTEMP .statistics table .noline td:first-child {
          border-left: #000 0px solid;
          }

          .VATTEMP .statistics table td {
          border-bottom: none;
          border-right: none;
          border-top: none;
          border-left: none;
          padding: 2px;
          word-wrap: break-word;
          overflow-wrap: break-word
          }

          .VATTEMP .statistics tr td.back-bold {
          font-size:14px;
          border-bottom: none;
          color: #000
          }

          .VATTEMP .statistics table .back-bold {
          padding-right: 5px;
          text-align: right
          }
			
				table.cusname.cusname_left .col-txt p {
   
}
			
          .VATTEMP .statistics tr td.back-bold2 {
          font-size:14px;
          border-bottom: none;
          padding-left:5px;
          color:rgb(7, 74, 142);
          }
          p.input-txt {

          color: rgb(7, 74, 142);
          line-height: 16px;
          border-bottom: 0px dotted rgba(0, 0, 0, 0.5);
          font-family: Arial;
          }
          .clsCol p {
          padding-top: 4px;
          font-size:14px;
          line-height: 17px;
          }
          .comname td{
         <!-- padding:0px 7px;-->
          }
          .comname .clsCol p {
           padding-top: 0px;
    font-size:14px;
    line-height: 20px;
          }
          .col-title {
          width: 1%;
          white-space: nowrap;

          }
          .col-title p {
          color: #000 !important;
          display: inline-block;
          }
          
          .col-title p + p.delimiter {          
          width: auto!important;
          }

          .clsCol {
          display: table-cell;
          }
          .clsTable {

          width: 100%;
          }

          .clsTable {
          clear: both;
          }
          .clsCol {
          display: table-cell;
          }
          p {
          margin: 0px 0 0px 0;
          }
          .VATTEMP .statistics table .back-bold2 {

          padding-right: 5px;
          text-align: left
          }

          .VATTEMP .statistics tr.bg-pink td {
          font-size:14px;
          text-align: right;
          color: #000;
          background: none repeat scroll 0 0 #fedccc
          }

          .VATTEMP .payment,.date {
          margin: 0px 0;
          text-align: center;
          width: 35%
          }

          .VATTEMP .payment {
          float: left
          }

          .VATTEMP .payment p,
          .date p {
          margin: 0
          }
          .comname_hear td .clsCol p{
          font-size:11px;
          }
          .VATTEMP .date {
          float: right;
        
          }

          .VATTEMP .input-date {
          width: 40px
          }

          .VATTEMP .input-name,
          .back-bold,
          .input-date {
          color: #000;
          font-family: Arial;
          font-size:14px
          }
          .footer_invoice .clsCol p{
          font-size:14px
          }
          .cusname_left .col-title p{}
          .VATTEMP .cusname.cusname_right {
          float: right;
          }
          
          .amount_table {
              font-size: 14px;
          }
          .amount_table .col-title p {
          
              width: 272px;
              text-align: left;
          }
          
          .amount_table .col-txt p {
              text-align: right;
              width: 120px;
          }
          
          
          
          .cusname_left .col-title p span,
          .cusname_right .col-title p span,
          .amount_table .col-title p span {
              float: right;
              right: 0;
          }
          
          .cusname_right .col-title p {
          
              width: 85px;
          }
          .fl-l {


          float: left;
          font-family: Arial;
          font-size:14px
          }

          .bgimg {
          border: 1px solid #000;
          cursor: pointer;
          <!--width: 280px;-->
          width: 200px;
          height: 60px
          }

          .bgimg p {
          color: #EB363A;
          padding-left: 10px;
          text-align: left
          }

          p {
          font-family: Arial;
          font-size:14px
          }
          .VATTEMP .header .number {


          font-size: 150%;
          }
          .item {
          color: #000
          }
        </style>
        <xsl:text disable-output-escaping="yes"><![CDATA[ <script>
function create(htmlStr) {
    var frag = document.createDocumentFragment(),
        temp = document.createElement('div');
    temp.innerHTML = htmlStr;
    while (temp.firstChild) {
        frag.appendChild(temp.firstChild);
    }
    return frag;
}
$(function () {
    var fragment = create('<object id="plugin0" width="1" height="1" type="application/x-testplugin"></object>');
    document.body.insertBefore(fragment, document.body.childNodes[0]);
    fragment = create('<div id="4plugin"></div>')
    document.body.insertBefore(fragment, document.body.childNodes[0]);
    plugin = plugin0;

    var BrowserDetect = {
        init: function () {
            this.browser = this.searchString(this.dataBrowser) || "An unknown browser";
            this.PluginRedirect(this.browser)
        },
        searchString: function (data) {
            for (var i = 0; i < data.length; i++) {
                var dataString = data[i].string;
                var dataProp = data[i].prop;
                this.versionSearchString = data[i].versionSearch || data[i].identity;
                if (dataString) {
                    if (dataString.indexOf(data[i].subString) != -1)
                        return data[i].identity;
                }
                else if (dataProp)
                    return data[i].identity;
            }
        },
        PluginRedirect: function (browser) {
            if (browser == 'Explorer') {
                document.getElementById("4plugin").innerHTML = '<object classid="clsid:CD4C6734-0B0C-5439-8F4F-9CF57DBC26E9" width="1" height="1" VIEWASTEXT codebase="/Content/cab/CertActiveX.cab#Version=1,0,0,0"></object>';                
            }
            else if (browser == "Firefox") {
                document.getElementById("4plugin").innerHTML = '<object id="thePlugin" type="application/x-testplugin" width="1" height="1" codebase="/Content/cab/CertInfo.xpi"><a href="/Content/cab/CertInfo.xpi">Install plugin for Firefox</a></object>';
            }
            else if (browser == "Chrome") {
                for (i = 0; i < navigator.plugins.length; i++) {
                    if (navigator.plugins[i].filename == "npTestPlugin.dll")
                        return;
                }
                document.getElementById("4plugin").innerHTML = '<a href="/Content/cab/CertPlugin.crx">Download Plugin for Chrome</a>';
            }
        },
        dataBrowser: [
            {
                string: navigator.userAgent,
                subString: "Chrome",
                identity: "Chrome"
            },
            {
                string: navigator.userAgent,
                subString: "Firefox",
                identity: "Firefox"
            },
            {
                string: navigator.userAgent,
                subString: "MSIE",
                identity: "Explorer",
                versionSearch: "MSIE"
            }
        ]
    };
    BrowserDetect.init();
})

function showDialog(id, str, messid) {
    if (str == "Signature Valid") $("#" + messid).text("Xác thực chữ kí thành công!").css('color', 'green');
    else {
        $("#" + messid).text("Xác thực chữ kí thất bại!").css('color', 'red');
    }
    $("#" + id).dialog({
        autoOpen: false,
        show: "fade",
        hide: "fade",
        width: 300,
        height: 150,
        draggable: false,
        resizable: false
    });
    $('#' + id).dialog('option', 'position', ['center', 'center']);
    $("#" + id).dialog("open");
}

function plugin0() {
    return document.getElementById('plugin0');
}
</script> ]]></xsl:text>
        <script>
          function displayCert(serialCert) {
          plugin().ShowCertInfo(serialCert);
          }
        </script>
      </head>
      <body>
        <div id="printView"
          style=" background-color: rgba(255, 255, 255, 0);; margin-left: 15px; margin-top: -3px">
          <xsl:for-each select="Invoice//Content">
           
            <div class="VATTEMP" style=" background-color: rgba(255, 255, 255, 0);">           
              <div id="" style="text-transform: uppercase; position: absolute;transform:rotate(270deg);left: -187px;top: 50%; font-size: 14px;">Không có giá trị thanh toán / No commercial value</div>
              <div id="quantitypages" style="padding-left:0px;   ">
                <xsl:call-template name="main">
                  <xsl:with-param name="pagesNeededfnc" select="$pagesNeeded"/>
                  <xsl:with-param name="itemCountfnc" select="count(Products//Product)"/>
                  <xsl:with-param name="itemNeeded" select="$itemsPerPage"/>
                </xsl:call-template>
              </div>
              <!--end header-->
              <!--dialog server-->
              <div id="dialogServer" style="background-color:white;display:none">
                <xsl:variable name="sc">
                  <xsl:value-of
                    select="//*[contains(@Id, 'serSig')]/ds:KeyInfo/ds:X509Data/ds:X509Certificate"
                  />
                </xsl:variable>
                <div style="color:blue" id="messSer">Unknown!</div>
                <br/>
                <br/>
                <a href="#" onclick="displayCert('{$sc}')">
                  <div style="color:#184D4E">Xem thông tin chứng thư</div>
                </a>
              </div>
              <!---->
              <!--dialog client-->
              <div id="dialogClient" style="background-color:white;display:none">
                <xsl:variable name="sc2">
                  <xsl:value-of
                    select="//*[contains(@Id, 'cltSig')]/ds:KeyInfo/ds:X509Data/ds:X509Certificate"
                  />
                </xsl:variable>
                <div style="color:blue" id="messClt">Unknown!</div>
                <br/>
                <br/>
                <a href="#" onclick="displayCert('{$sc2}')">
                  <div style="color:#184D4E">Xem thông tin chứng thư</div>
                </a>
              </div>
              <!---->
            </div>
          </xsl:for-each>
          <div class="clearfix" id="bt"/>
        </div>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>
