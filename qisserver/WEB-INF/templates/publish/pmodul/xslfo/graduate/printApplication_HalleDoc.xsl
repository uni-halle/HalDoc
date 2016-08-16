<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:date="http://exslt.org/dates-and-times" extension-element-prefixes="date"
	xmlns:svg="http://www.w3.org/2000/svg"
	xmlns:dyn="http://exslt.org/dynamic" version="1.0">

	<xsl:output method="xml" indent="yes" />
	<xsl:decimal-format name="FrenchDecimalFormat" decimal-separator="." grouping-separator="&#160;" />
	<xsl:decimal-format name="european" decimal-separator="." grouping-separator="," />
	<xsl:variable name="now" select="date:date-time()" />

	<xsl:template match="publishDetail">

		<xsl:variable name="imagePath">file:[PUBROOT]img</xsl:variable>
		<!--xsl:variable name="imagePath">D:/Tomcats/tomcat-6.0.39-14.0/webapps/qisserver/pub/img</xsl:variable-->
		<xsl:variable name="fakId"><xsl:value-of select="/publishDetail/Applicant/Promotion/einrichtungid" /></xsl:variable>
		<xsl:variable name="fakName">
			<xsl:for-each select="Applicant/Listen/Faks/Fak">
				<xsl:if test="id=$fakId">
					<xsl:value-of select="ltxt" />
				</xsl:if>
			</xsl:for-each>
		</xsl:variable>
		<xsl:variable name="viereckColor">
			<xsl:for-each select="Applicant/Listen/Faks/Fak">
				<xsl:if test="id=$fakId">
					<xsl:value-of select="farbe" />
				</xsl:if>
			</xsl:for-each>
		</xsl:variable>
		<xsl:variable name="font">ZapfHumanist601BT-Roman</xsl:variable>

		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="ersteSeite" page-height="297mm" page-width="210mm">
					<fo:region-body region-name="Body" margin-top="50mm" margin-left="30mm" margin-right="15mm" margin-bottom="15mm" background-color="white" />
					<fo:region-before region-name="KopfSeite1" extent="45mm" precedence="true" background-color="white" />
					<fo:region-after region-name="Fuss" extent="15mm" background-color="white" />
					<fo:region-start region-name="LRand" extent="50mm" background-color="white" />
					<fo:region-end region-name="RRand" extent="15mm" background-color="white" />
				</fo:simple-page-master>
				<fo:simple-page-master master-name="weitereSeiten" page-height="297mm" page-width="210mm">
					<fo:region-body region-name="Body" margin-top="15mm" margin-left="30mm" margin-right="15mm" margin-bottom="15mm" background-color="white" />
					<fo:region-before region-name="Kopf" extent="14mm" precedence="true" background-color="white" />
					<fo:region-after region-name="Fuss" extent="15mm" background-color="white" />
					<fo:region-start region-name="LRand" extent="50mm" background-color="white" />
					<fo:region-end region-name="RRand" extent="15mm" background-color="white" />
				</fo:simple-page-master>
				<fo:page-sequence-master master-name="global">
					<fo:single-page-master-reference master-reference="ersteSeite" />
					<fo:repeatable-page-master-reference master-reference="weitereSeiten" />
				</fo:page-sequence-master>
			</fo:layout-master-set>

			<fo:page-sequence master-reference="global">
				<!-- NEU NEU NEU -->
				<fo:static-content flow-name="KopfSeite1">
					<fo:block-container position="absolute" left="115mm" top="0mm" height="22mm" width="27mm" border-bottom-style="solid" border-bottom-width="0.5pt">
						<fo:block space-before="6mm" padding-top="6mm">
							<fo:external-graphic src="{$imagePath}/image_siegel_Halle.gif" content-height="13mm" content-width="25mm">
							</fo:external-graphic>
						</fo:block>
					</fo:block-container>
					<fo:block-container position="absolute" left="134mm" top="26mm" height="5mm" width="5mm" background-color="#{$viereckColor}">
						<fo:block />
					</fo:block-container>
					<fo:block-container position="absolute" left="142mm" top="0mm" height="22mm" width="68mm" border-left-style="solid" border-bottom-style="solid" border-left-width="0.5pt"
						border-bottom-width="0.5pt">
						<fo:block padding-top="10mm" margin-left="2mm" space-before="10mm" font-family="{$font}" font-size="12pt">
							MARTIN-LUTHER-UNIVERSITÄT
						</fo:block>
						<fo:block margin-left="2mm" space-before="0mm" font-family="{$font}" font-size="12pt">
							HALLE-WITTENBERG
						</fo:block>
					</fo:block-container>
					<!-- name der fakultät und des bereichs -->
					<fo:block-container position="absolute" left="142mm" top="20mm" height="25mm" width="68mm" border-left-style="solid" border-left-width="0.5pt">
						<fo:block padding-top="4mm" margin-left="2mm" space-before="4mm" font-family="{$font}" font-size="10pt" hyphenate="true" language="de" margin-right="5mm">
							<xsl:value-of select="$fakName" />
						</fo:block>
					</fo:block-container>
				</fo:static-content>
				<!-- NEU NEU NUE -->

				<fo:static-content flow-name="Kopf">
					<fo:block-container position="absolute" left="15mm" top="4mm">
						<fo:table table-layout="fixed">
							<fo:table-column column-width="55.5mm" />
							<fo:table-column column-width="132mm" />
							<fo:table-body>

								<fo:table-row font-size="8.5pt">
									<fo:table-cell border="0.0pt solid green" padding-top="1mm">
										<fo:block font-family="{$font}">
											Martin-Luther-Universität Halle-Wittenberg
										</fo:block>
									</fo:table-cell>

									<fo:table-cell border="0.0pt dotted yellow" padding-top="1mm">
										<fo:block font-family="{$font}" text-align="end">
											<xsl:value-of select="$fakName" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>

								<!-- linie -->
								<fo:table-row keep-with-previous="always" keep-with-next="always" keep-together="always" page-break-after="avoid">
									<fo:table-cell padding-top="0.2pt" border-top-style="solid" border-color="#a0a0a0" border-width="0.8pt" number-columns-spanned="2">
										<fo:block color="#ffffff" text-align="start" />
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block-container>
				</fo:static-content>

				<!-- Falzmarken immer am linken Rand -->
				<fo:static-content flow-name="LRand">
					<fo:block-container absolute-position="fixed" top="101.5mm" left="3.5mm">
						<fo:block>
							<fo:leader leader-pattern="rule" rule-thickness="0.5pt" leader-length="3mm" />
						</fo:block>
						<fo:block padding-top="38.5mm">
							<fo:leader leader-pattern="rule" rule-thickness="0.5pt" leader-length="6mm" />
						</fo:block>
						<fo:block padding-top="56.5mm">
							<fo:leader leader-pattern="rule" rule-thickness="0.5pt" leader-length="3mm" />
						</fo:block>
					</fo:block-container>
				</fo:static-content>

				<fo:static-content flow-name="Fuss">
					<fo:block-container position="absolute" left="20mm" height="15mm" width="40mm" border-left-style="solid" border-left-width="0.5pt" color="#000000" text-align="start" font-family="{$font}" font-size="8pt" line-height="10pt">
						<fo:block width="40mm" margin-left="3mm">
							Hausanschrift:
						</fo:block>
						<fo:block width="40mm" margin-left="3mm" />
						<fo:block width="40mm" margin-left="3mm" />
					</fo:block-container>
					<fo:block-container position="absolute" left="60mm" height="15mm" width="40mm" border-left-style="solid" border-left-width="0.5pt" color="#000000" text-align="start" font-family="{$font}" font-size="8pt" line-height="10pt">
						<fo:block width="40mm" margin-left="3mm">
							<xsl:text>Tel </xsl:text>
						</fo:block>
						<fo:block width="40mm" margin-left="3mm">
							<xsl:text>Fax   </xsl:text>
						</fo:block>
					</fo:block-container>
					<fo:block-container position="absolute" left="100mm" height="15mm" width="40mm" border-left-style="solid" border-left-width="0.5pt" color="#000000" text-align="start" font-family="{$font}" font-size="8pt" line-height="10pt">
						<fo:block margin-left="3mm">
							e-mail:
						</fo:block>
						<fo:block margin-left="3mm" hyphenate="true" language="de" />
					</fo:block-container>
					<fo:block-container position="absolute" left="140mm" height="15mm" width="40mm" border-left-style="solid" border-left-width="0.5pt" color="#000000" text-align="start" font-family="{$font}" font-size="8pt" line-height="10pt">
						<fo:block margin-left="3mm">
							Internet:
						</fo:block>
						<fo:block margin-left="3mm" hyphenate="true" language="de" />
					</fo:block-container>
				</fo:static-content>

				<fo:flow flow-name="Body" font-family="{$font}" font-size="10pt">
					<fo:block-container absolute-position="auto" break-before="auto">
						<fo:block color="#000000" text-align="start" font-family="{$font}" font-weight="bold" hyphenate="true" language="de" padding-bottom="4em">
							Antrag auf Annahme als Doktorandin bzw. Doktorand gemäß § 5 der Promotionsordnung der Einrichtung <xsl:value-of select="$fakName" /> an der Martin-Luther-Universität Halle-Wittenberg
						</fo:block>
						<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="de" padding-bottom="1em">
							im Institut <xsl:value-of select="Applicant/Promotion/institute" />
						</fo:block>
						<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="de" padding-bottom="2em">
							In Kenntnis der Bestimmungen über die Voraussetzungen und Verfahrensfragen zur Promotion beantrage ich hiermit die Annahme als Doktorandin bzw. Doktorand.
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block>Persönliche Daten:</fo:block>
							<fo:block><xsl:text>Name und Geburtsname: </xsl:text><xsl:value-of select="Applicant/nachname" /></fo:block>
							<fo:block><xsl:text>Vorname: </xsl:text><xsl:value-of select="Applicant/vorname" /></fo:block>
							<fo:block><xsl:text>Geschlecht: </xsl:text><xsl:value-of select="Applicant/geschl" /></fo:block>
							<fo:block><xsl:text>Geburtsdatum: </xsl:text><xsl:value-of select="Applicant/gebdat" /></fo:block>
							<fo:block><xsl:text>Geburtsort: </xsl:text><xsl:value-of select="Applicant/gebort" /></fo:block>
							<fo:block><xsl:text>Geburtsland: </xsl:text><xsl:value-of select="Applicant/Geburtsland" /></fo:block>
							<fo:block><xsl:text>Staatsangehörigkeit: </xsl:text><xsl:value-of select="Applicant/Nationality" /></fo:block>
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block>Wohnanschrift:</fo:block>
							<fo:block><xsl:text>Straße: </xsl:text><xsl:value-of select="Applicant/AddressPrivate/strasse" /></fo:block>
							<fo:block><xsl:text>PLZ - Ort: </xsl:text><xsl:value-of select="Applicant/AddressPrivate/plz" /><xsl:text> - </xsl:text><xsl:value-of select="Applicant/AddressPrivate/ort" /></fo:block>
							<fo:block><xsl:text>Land: </xsl:text><xsl:value-of select="Applicant/AddressPrivate/Land" /></fo:block>
							<fo:block><xsl:text>Telefonnummer: </xsl:text><xsl:value-of select="Applicant/AddressPrivate/telefon" /></fo:block>
							<fo:block><xsl:text>E-Mail-Adresse: </xsl:text><xsl:value-of select="Applicant/AddressPrivate/email" /></fo:block>
						</fo:block>
					</fo:block-container>
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="de" padding-bottom="2em">
							<fo:table>
								<fo:table-column column-width="1.5cm"/>
								<fo:table-column column-width="14.5cm"/>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2"><fo:block><xsl:text>Aktueller Status der Antragstellerin bzw. des Antragsstellers</xsl:text></fo:block></fo:table-cell>
									</fo:table-row>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Wissenschaftliche Mitarbeiterin bzw. wissenschaftlicher Mitarbeiter der Universität Halle (Haushalt)</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="Applicant/applicantstatusid = '1'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">0</xsl:with-param>
										<xsl:with-param name="additional">0</xsl:with-param>
									</xsl:call-template>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Wissenschaftliche Mitarbeiterin bzw. wissenschaftlicher Mitarbeiter der Universität Halle (Drittmittel)</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="Applicant/applicantstatusid = '2'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">0</xsl:with-param>
										<xsl:with-param name="additional">0</xsl:with-param>
									</xsl:call-template>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Stipendiatin bzw. Stipendiat</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="Applicant/applicantstatusid = '3'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">0</xsl:with-param>
										<xsl:with-param name="additional">0</xsl:with-param>
									</xsl:call-template>
									<xsl:variable name="businessAddress1">
										<xsl:if test="Applicant/applicantstatusid = '4'">
											<xsl:value-of select="Applicant/AddressBusiness/empName" /><xsl:text>, </xsl:text><xsl:value-of select="Applicant/AddressBusiness/strasse" /><xsl:text>, </xsl:text><xsl:value-of select="Applicant/AddressBusiness/plz" /> <xsl:value-of select="Applicant/AddressBusiness/ort" /><xsl:text>, </xsl:text><xsl:value-of select="Applicant/AddressBusiness/telefon" />
										</xsl:if>
									</xsl:variable>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Externe Doktorandin bzw. externer Doktorand</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="Applicant/applicantstatusid = '4'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">gegebenenfalls Institution, Adresse und Tel.-Nr.:</xsl:with-param>
										<xsl:with-param name="additional"><xsl:choose><xsl:when test="normalize-space($businessAddress1) = ''">0</xsl:when><xsl:otherwise><xsl:value-of select="$businessAddress1" /></xsl:otherwise></xsl:choose></xsl:with-param>
									</xsl:call-template>
									<xsl:variable name="businessAddress2">
										<xsl:if test="Applicant/applicantstatusid = '5'">
											<xsl:value-of select="Applicant/AddressBusiness/empName" /><xsl:text>, </xsl:text><xsl:value-of select="Applicant/AddressBusiness/strasse" /><xsl:text>, </xsl:text><xsl:value-of select="Applicant/AddressBusiness/plz" /> <xsl:value-of select="Applicant/AddressBusiness/ort" /><xsl:text>, </xsl:text><xsl:value-of select="Applicant/AddressBusiness/telefon" />
										</xsl:if>
									</xsl:variable>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Anderes</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="Applicant/applicantstatusid = '5'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">gegebenenfalls Institution, Adresse und Tel.-Nr.:</xsl:with-param>
										<xsl:with-param name="additional"><xsl:choose><xsl:when test="normalize-space($businessAddress2) = ''">0</xsl:when><xsl:otherwise><xsl:value-of select="$businessAddress2" /></xsl:otherwise></xsl:choose></xsl:with-param>
									</xsl:call-template>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block-container>
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="de" padding-bottom="2em">
							<fo:table>
								<fo:table-column column-width="1.5cm"/>
								<fo:table-column column-width="14.5cm"/>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2"><fo:block>Die Erarbeitung der Dissertation erfolgt im Rahmen eines strukturierten Promotionsprogrammes.</fo:block></fo:table-cell>
									</fo:table-row>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Ja</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="normalize-space(Applicant/Promotion/promoprogramm) = 'J'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">gegebenenfalls Institution und Bezeichnung des Programmes:</xsl:with-param>
										<xsl:with-param name="additional"><xsl:choose><xsl:when test="normalize-space(Applicant/Promotion/promoprogrammdetail) = ''">0</xsl:when><xsl:when test="normalize-space(Applicant/Promotion/promoprogramm) != 'J'">0</xsl:when><xsl:otherwise><xsl:value-of select="Applicant/Promotion/promoprogrammdetail" /></xsl:otherwise></xsl:choose></xsl:with-param>
									</xsl:call-template>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Nein</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="normalize-space(Applicant/Promotion/promoprogramm) = 'J'">0</xsl:when><xsl:otherwise>1</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">0</xsl:with-param>
										<xsl:with-param name="additional">0</xsl:with-param>
									</xsl:call-template>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block-container>
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="de" padding-bottom="2em">
							<fo:table>
								<fo:table-column column-width="1.5cm"/>
								<fo:table-column column-width="14.5cm"/>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2"><fo:block>Die Erarbeitung der Dissertation erfolgt im Rahmen eines bi-nationalen Promotionsverfahrens (Cotutelle de thèse).</fo:block></fo:table-cell>
									</fo:table-row>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Ja</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="normalize-space(Applicant/Promotion/framework) = 'J'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">gegebenenfalls Institution und Land:</xsl:with-param>
										<xsl:with-param name="additional"><xsl:choose><xsl:when test="normalize-space(Applicant/Promotion/frameworkdetail) = ''">0</xsl:when><xsl:when test="normalize-space(Applicant/Promotion/framework) != 'J'">0</xsl:when><xsl:otherwise><xsl:value-of select="Applicant/Promotion/frameworkdetail" /></xsl:otherwise></xsl:choose></xsl:with-param>
									</xsl:call-template>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Nein</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="normalize-space(Applicant/Promotion/framework) = 'J'">0</xsl:when><xsl:otherwise>1</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">0</xsl:with-param>
										<xsl:with-param name="additional">0</xsl:with-param>
									</xsl:call-template>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block-container>
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block>Bereits erworbene akademische Grade:</fo:block>
							<fo:table>
								<fo:table-column column-width="4cm"/>
								<fo:table-column column-width="3.5cm"/>
								<fo:table-column column-width="4cm"/>
								<fo:table-column column-width="4.5cm"/>
								<fo:table-header>
									<fo:table-row>
										<fo:table-cell><fo:block>Akad. Grad</fo:block></fo:table-cell>
										<fo:table-cell><fo:block>Wann erreicht</fo:block></fo:table-cell>
										<fo:table-cell><fo:block>Wo erreicht</fo:block></fo:table-cell>
										<fo:table-cell><fo:block>An einer Fachhochschule?</fo:block></fo:table-cell>
									</fo:table-row>
								</fo:table-header>
								<fo:table-body>
									<xsl:for-each select="Applicant/Degrees/Degree">
										<fo:table-row>
											<fo:table-cell><fo:block><xsl:value-of select="AkadGrad" /></fo:block></fo:table-cell>
											<fo:table-cell><fo:block><xsl:value-of select="date" /></fo:block></fo:table-cell>
											<fo:table-cell><fo:block><xsl:value-of select="place" /></fo:block></fo:table-cell>
											<fo:table-cell><fo:block>
												<xsl:choose>
													<xsl:when test="isappliedsience = 'X'">Ja</xsl:when>
													<xsl:otherwise>Nein</xsl:otherwise>
												</xsl:choose>
											</fo:block></fo:table-cell>
										</fo:table-row>
									</xsl:for-each>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block-container>
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block>Fachgebiet der angestrebten Promotion (gemäß Fächerkatalog der Promotionsordnung der Einrichtung):</fo:block>
							<fo:block><xsl:value-of select="Applicant/Promotion/Fach" /></fo:block>
						</fo:block>
					</fo:block-container>
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block>Angestrebter akademischer Grad:</fo:block>
							<fo:table>
								<fo:table-column column-width="1.5cm"/>
								<fo:table-column column-width="14.5cm"/>
								<fo:table-body>
									<xsl:for-each select="Applicant/Listen/AkadGrades/AkadGrade">
										<xsl:call-template name="checkField">
											<xsl:with-param name="text"><xsl:value-of select="ltxt" /> (<xsl:value-of select="dtxt" />)</xsl:with-param>
											<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="gradid = /publishDetail/Applicant/Promotion/gradid">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
											<xsl:with-param name="additionalPre">0</xsl:with-param>
											<xsl:with-param name="additional">0</xsl:with-param>
										</xsl:call-template>
									</xsl:for-each>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block-container>
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block><xsl:text>Arbeitstitel der Dissertation: </xsl:text><xsl:value-of select="Applicant/Promotion/title" /></fo:block>
							<fo:block><xsl:text>Beginn der Arbeit an der Dissertation: </xsl:text><xsl:value-of select="Applicant/Promotion/startofdoctoralstudies" /></fo:block>
						</fo:block>
					</fo:block-container>
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:table>
								<fo:table-column column-width="6.5cm"/>
								<fo:table-column column-width="9.5cm"/>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2"><fo:block>Betreuende Hochschullehrerin bzw. betreuender Hochschullehrer (1):</fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell><fo:block>Akad. Titel, akad. Grad, Name, Vorname:</fo:block></fo:table-cell>
										<fo:table-cell><fo:block><xsl:value-of select="Applicant/Promotion/mentor1name" /></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell><fo:block>Wissenschaftl. Einrichtung, Institut:</fo:block></fo:table-cell>
										<fo:table-cell><fo:block><xsl:value-of select="Applicant/Promotion/mentor1einrich" /></fo:block></fo:table-cell>
									</fo:table-row>
									<xsl:if test="normalize-space(Applicant/Promotion/mentor2name) != ''">
										<fo:table-row>
											<fo:table-cell number-columns-spanned="2" padding-top="1em">
												<fo:block>Gegebenenfalls:</fo:block>
												<fo:block>Betreuende Hochschullehrerin bzw. betreuender Hochschullehrer (2):</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell><fo:block>Akad. Titel, akad. Grad, Name, Vorname:</fo:block></fo:table-cell>
											<fo:table-cell><fo:block><xsl:value-of select="Applicant/Promotion/mentor2name" /></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell><fo:block>Wissenschaftl. Einrichtung, Institut:</fo:block></fo:table-cell>
											<fo:table-cell><fo:block><xsl:value-of select="Applicant/Promotion/mentor2einrich" /></fo:block></fo:table-cell>
										</fo:table-row>
									</xsl:if>
									<xsl:if test="normalize-space(Applicant/Promotion/mentor3name) != ''">
										<fo:table-row>
											<fo:table-cell number-columns-spanned="2" padding-top="1em">
												<fo:block>Gegebenenfalls:</fo:block>
												<fo:block>Betreuende Hochschullehrerin bzw. betreuender Hochschullehrer (3):</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell><fo:block>Akad. Titel, akad. Grad, Name, Vorname:</fo:block></fo:table-cell>
											<fo:table-cell><fo:block><xsl:value-of select="Applicant/Promotion/mentor3name" /></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell><fo:block>Wissenschaftl. Einrichtung, Institut:</fo:block></fo:table-cell>
											<fo:table-cell><fo:block><xsl:value-of select="Applicant/Promotion/mentor3einrich" /></fo:block></fo:table-cell>
										</fo:table-row>
									</xsl:if>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block-container>
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always" padding-bottom="1em">
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de">Kenntnisnahme und Bestätigung der Bereitschaft der betreuenden Hochschullehrerin bzw. des betreuenden Hochschullehrers / Confirmation of willingness of the mentoring professor (1)</fo:block>
						<fo:block font-size="8pt" padding-top="6em" padding-bottom="1em">Datum / Date  Unterschrift der Betreuerin bzw. des Betreuers / Signature of the mentor (1)</fo:block>
					</fo:block-container>
					<xsl:if test="normalize-space(Applicant/Promotion/mentor2name) != ''">
						<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
							<fo:block hyphenate="true" language="de">Kenntnisnahme und Bestätigung der Bereitschaft der betreuenden Hochschullehrerin bzw. des betreuenden Hochschullehrers / Confirmation of willingness of the mentoring professor (2) </fo:block>
							<fo:block font-size="8pt" padding-top="6em" padding-bottom="1em">Datum / Date  Unterschrift der Betreuerin bzw. des Betreuers / Signature of the mentor (2)</fo:block>
						</fo:block-container>
					</xsl:if>
					<xsl:if test="normalize-space(Applicant/Promotion/mentor3name) != ''">
						<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
							<fo:block hyphenate="true" language="de">Kenntnisnahme und Bestätigung der Bereitschaft der betreuenden Hochschullehrerin bzw. des betreuenden Hochschullehrers / Confirmation of willingness of the mentoring professor (3) </fo:block>
							<fo:block font-size="8pt" padding-top="6em" padding-bottom="1em">Datum / Date  Unterschrift der Betreuerin bzw. des Betreuers / Signature of the mentor (3)</fo:block>
						</fo:block-container>
					</xsl:if>
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block>Kenntnisnahme der Leiterin bzw. des Leiters des betreffenden Instituts bzw. der betreffenden Klinik</fo:block>
							<fo:block font-size="8pt" padding-top="6em">Datum und Unterschrift der Leiterin bzw. des Leiters</fo:block>
						</fo:block>
					</fo:block-container>
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block>Ich erkläre, dass ich mich an keiner anderen Hochschule einem Promotionsverfahren unterzogen bzw. eine Promotion begonnen habe. / I declare that I have not completed or initiated a doctorate procedure at any other university.</fo:block>
							<fo:block font-size="8pt" padding-top="6em">Ort / Place, Datum / Date</fo:block>
							<fo:block font-size="8pt">Unterschrift der Antragstellerin bzw. des Antragstellers / Signature of the applicant</fo:block>
						</fo:block>
					</fo:block-container>
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block>Anlagen / Enclosures:</fo:block>
							<fo:block>
								<fo:list-block>
									<fo:list-item>
										<fo:list-item-label end-indent="label-end()"><fo:block>-</fo:block></fo:list-item-label>
										<fo:list-item-body start-indent="body-start()"><fo:block>Erklärung / Declaration</fo:block></fo:list-item-body>
									</fo:list-item>
									<fo:list-item>
										<fo:list-item-label end-indent="label-end()"><fo:block>-</fo:block></fo:list-item-label>
										<fo:list-item-body start-indent="body-start()"><fo:block>Lebenslauf der Antragstellerin bzw. des Antragstellers / CV of the applicant</fo:block></fo:list-item-body>
									</fo:list-item>
									<fo:list-item>
										<fo:list-item-label end-indent="label-end()"><fo:block>-</fo:block></fo:list-item-label>
										<fo:list-item-body start-indent="body-start()"><fo:block>Kopien aller Zeugnisse über die erreichten Studienabschlüsse (beglaubigte Kopien oder Vorlage der Originale) / Copies of all final degree certificates (notarised copies or originals)</fo:block></fo:list-item-body>
									</fo:list-item>
									<fo:list-item>
										<fo:list-item-label end-indent="label-end()"><fo:block>-</fo:block></fo:list-item-label>
										<fo:list-item-body start-indent="body-start()"><fo:block>Gegebenenfalls Äquivalenzbescheinigung / Certificate of equivalence, if applicable</fo:block></fo:list-item-body>
									</fo:list-item>
								</fo:list-block>
							</fo:block>
						</fo:block>
					</fo:block-container>
					<fo:block break-after="page" />
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							<fo:block font-weight="bold">Bearbeitungsbogen der Einrichtung <xsl:value-of select="$fakName" /> / Editing form of the <xsl:value-of select="$fakName" /></fo:block>
							<fo:block>(Wird von der Einrichtung <xsl:value-of select="$fakName" /> ausgefüllt / To be filled in by the <xsl:value-of select="$fakName" />)</fo:block>
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							<fo:block>Die gemäß der Promotionsordnung der Einrichtung <xsl:value-of select="$fakName" /> einzureichenden Unterlagen wurden vollzählig und ordnungsgemäß vorgelegt.</fo:block>
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							<fo:block font-size="8pt" padding-top="6em">Ort, Datum, Unterschrift der Beauftragten bzw. des Beauftragten der Dekanin bzw. des Dekans</fo:block>
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							<fo:block font-weight="bold">Prüfung und Zustimmung durch den Promotionsausschuss </fo:block>
							<fo:table>
								<fo:table-column column-width="1.5cm"/>
								<fo:table-column column-width="14.5cm"/>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell><fo:block text-align="center"><fo:inline font-family="ZapfDingbats">&#x274F;</fo:inline></fo:block></fo:table-cell>
										<fo:table-cell><fo:block>Dem Antrag auf Annahme als Doktorandin bzw. Doktorand für das Fachgebiet <xsl:value-of select="Applicant/Promotion/Fach" /> wird entsprochen.</fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell><fo:block /></fo:table-cell>
										<fo:table-cell padding-top="1em"><fo:block><fo:block>Die Zulassung zum Promotionsverfahren gemäß § 5 der Promotionsordnung der Einrichtung <xsl:value-of select="$fakName" /> an der Martin-Luther-Universität Halle-Wittenberg wird nur gewährt, wenn zur Erfüllung der Zulassungsvoraussetzungen zur Promotion gemäß § 3 der Promotionsordnung zum Zeitpunkt des Antrages auf Zulassung zum Promotionsverfahren der Nachweis über folgende Leistungen erbracht wird:</fo:block></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell><fo:block /></fo:table-cell>
										<fo:table-cell>
											<fo:block border-bottom-style="solid" border-bottom-width="0.5pt" padding-top="2.5em"/>
											<fo:block border-bottom-style="solid" border-bottom-width="0.5pt" padding-top="2.5em"/>
											<fo:block border-bottom-style="solid" border-bottom-width="0.5pt" padding-top="2.5em"/>
											<fo:block border-bottom-style="solid" border-bottom-width="0.5pt" padding-top="2.5em"/>
											<fo:block border-bottom-style="solid" border-bottom-width="0.5pt" padding-top="2.5em"/>
											<fo:block border-bottom-style="solid" border-bottom-width="0.5pt" padding-top="2.5em"/>
											<fo:block border-bottom-style="solid" border-bottom-width="0.5pt" padding-top="2.5em"/>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell padding-top="1em"><fo:block text-align="center"><fo:inline font-family="ZapfDingbats">&#x274F;</fo:inline></fo:block></fo:table-cell>
										<fo:table-cell padding-top="1em"><fo:block>Dem Antrag auf Annahme als Doktorandin bzw. Doktorand für das Fachgebiet <xsl:value-of select="Applicant/Promotion/Fach" /> wird nicht entsprochen. Der Mitteilung über die Ablehnung des Antrages liegt eine gesonderte Begründung und Rechtsbehelfsbelehrung bei.</fo:block></fo:table-cell>
									</fo:table-row>
								</fo:table-body>
							</fo:table>
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							<fo:block font-size="8pt" padding-top="6em">Ort, Datum, Unterschrift der Vorsitzenden bzw. des Vorsitzenden des Promotionsausschusses</fo:block>
						</fo:block>
					</fo:block-container>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	<xsl:template name="checkField">
		<xsl:param name="text" />
		<xsl:param name="isChecked" />
		<xsl:param name="additionalPre" />
		<xsl:param name="additional" />
		<xsl:variable name="checkbox">
			<xsl:choose>
				<xsl:when test="$isChecked = 1">&#x2713;</xsl:when>
				<xsl:otherwise>&#x274F;</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<fo:table-row>
			<fo:table-cell><fo:block text-align="center"><fo:inline font-family="ZapfDingbats"><xsl:value-of select="$checkbox" /></fo:inline></fo:block></fo:table-cell>
			<fo:table-cell><fo:block><xsl:value-of select="$text" /></fo:block></fo:table-cell>
		</fo:table-row>
		<xsl:if test="$additional != 0">
			<fo:table-row>
				<fo:table-cell><fo:block /></fo:table-cell>
				<fo:table-cell><fo:block><xsl:value-of select="$additionalPre" /><xsl:text> </xsl:text><xsl:value-of select="$additional" /></fo:block></fo:table-cell>
			</fo:table-row>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
