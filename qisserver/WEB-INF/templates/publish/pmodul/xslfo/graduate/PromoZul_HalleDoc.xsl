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
		<!--xsl:variable name="imagePath">D:/Tomcats/loewenportal170/webapps/portal/pub/img</xsl:variable-->
		<xsl:variable name="fakId"><xsl:value-of select="/publishDetail/application/pluginReturn/fillingData/open_promoFaculty" /></xsl:variable>
		<xsl:variable name="fakName">
			<xsl:for-each select="application/Faks/Fak">
				<xsl:if test="id=$fakId">
					<xsl:choose>
						<xsl:when test="contains(ltxt,'Naturwissenschaftliche')">
							<xsl:call-template name="string-replace-all">
						    <xsl:with-param name="text" select="ltxt" />
						    <xsl:with-param name="replace">Naturwissenschaftliche</xsl:with-param>
						    <xsl:with-param name="by">Naturwissenschaftlichen</xsl:with-param>
						  </xsl:call-template>
						</xsl:when>
						<xsl:when test="contains(ltxt,'Theologische')">
							<xsl:call-template name="string-replace-all">
						    <xsl:with-param name="text" select="ltxt" />
						    <xsl:with-param name="replace">Theologische</xsl:with-param>
						    <xsl:with-param name="by">Theologischen</xsl:with-param>
						  </xsl:call-template>
						</xsl:when>
						<xsl:when test="contains(ltxt,'Wirtschaftswissenschaftliche')">
							<xsl:call-template name="string-replace-all">
						    <xsl:with-param name="text" select="ltxt" />
						    <xsl:with-param name="replace">Juristische und Wirtschaftswissenschaftliche</xsl:with-param>
						    <xsl:with-param name="by">Juristischen und Wirtschaftswissenschaftlichen</xsl:with-param>
						  </xsl:call-template>
						</xsl:when>
						<xsl:when test="contains(ltxt,'Medizinische')">
							<xsl:call-template name="string-replace-all">
						    <xsl:with-param name="text" select="ltxt" />
						    <xsl:with-param name="replace">Medizinische</xsl:with-param>
						    <xsl:with-param name="by">Medizinischen</xsl:with-param>
						  </xsl:call-template>
						</xsl:when>
						<xsl:when test="contains(ltxt,'Philosophische')">
							<xsl:call-template name="string-replace-all">
						    <xsl:with-param name="text" select="ltxt" />
						    <xsl:with-param name="replace">Philosophische</xsl:with-param>
						    <xsl:with-param name="by">Philosophischen</xsl:with-param>
						  </xsl:call-template>
						</xsl:when>
						<xsl:when test="contains(ltxt,'Zentrum')">
							<xsl:call-template name="string-replace-all">
						    <xsl:with-param name="text" select="ltxt" />
						    <xsl:with-param name="replace">Zentrum</xsl:with-param>
						    <xsl:with-param name="by">Zentrums</xsl:with-param>
						  </xsl:call-template>
						</xsl:when>
						<xsl:otherwise><xsl:value-of select="ltxt" /></xsl:otherwise>
					</xsl:choose>
  			</xsl:if>
			</xsl:for-each>
		</xsl:variable>
		<xsl:variable name="fakNameEn">
			<xsl:for-each select="application/Faks/Fak">
				<xsl:if test="id=$fakId">
					<xsl:value-of select="ltxtEn" />
				</xsl:if>
			</xsl:for-each>
		</xsl:variable>
		<xsl:variable name="viereckColor">
			<xsl:for-each select="application/Faks/Fak">
				<xsl:if test="id=$fakId">
					<xsl:value-of select="farbe" />
				</xsl:if>
			</xsl:for-each>
		</xsl:variable>
		<xsl:variable name="gradNameForDoc">
			<xsl:for-each select="application/AkadGradesForPromotion/AkadGrade">
				<xsl:if test="gradid=/publishDetail/application/pluginReturn/fillingData/open_gettingGrade">
					<xsl:value-of select="ltxt" />
				</xsl:if>
			</xsl:for-each>
		</xsl:variable>
		<xsl:variable name="fachGebiet">
			<xsl:for-each select="application/courseOfStudies/courseOfStudy">
				<xsl:if test="AbsID=/publishDetail/application/pluginReturn/fillingData/open_courseOfStudy">
					<xsl:value-of select="AbsDtx" />
				</xsl:if>
			</xsl:for-each>
		</xsl:variable>
		<xsl:variable name="font">ZapfHumanist601BT-Roman</xsl:variable>

		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="ersteSeite" page-height="297mm" page-width="210mm">
					<fo:region-body region-name="Body" margin-top="15mm" margin-left="30mm" margin-right="15mm" margin-bottom="15mm" background-color="white" />
					<fo:region-before region-name="KopfSeite1" extent="14mm" precedence="true" background-color="white" />
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
				<fo:static-content flow-name="KopfSeite1">
					<fo:block-container position="absolute" left="15mm" top="4mm">
						<fo:block />
					</fo:block-container>
				</fo:static-content>

				<fo:static-content flow-name="Kopf">
					<fo:block-container position="absolute" left="15mm" top="4mm">
						<fo:block />
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

				<fo:static-content flow-name="Fuss" font-family="{$font}" font-size="8pt">
					<fo:block-container position="absolute" left="15mm" top="4mm">
						<fo:block text-align="right">
							Seite <fo:page-number/> von <fo:page-number-citation ref-id="{generate-id(.)}"/>
						</fo:block>
					</fo:block-container>
				</fo:static-content>

				<fo:flow flow-name="Body" font-family="{$font}" font-size="10pt">
					<xsl:variable name="artikel">
						<xsl:choose>
							<xsl:when test="contains($fakName,'Fakult')">der</xsl:when>
							<xsl:otherwise>des</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:variable name="paragraph">
						<xsl:choose>
							<xsl:when test="contains($fakName,'Medizinische')">5</xsl:when>
							<xsl:when test="contains($fakName,'Wirtschaftswiss. Bereich')">7</xsl:when>
							<xsl:otherwise>5</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<fo:block-container absolute-position="auto" break-before="auto">
						<xsl:choose>
							<xsl:when test="contains($fakName,'Juristischer Bereich')">
								<fo:block color="#000000" text-align="center" font-family="{$font}" font-size="12pt" font-weight="bold">
									Antrag auf Zulassung zum Promotionsverfahren gemäß § <xsl:value-of select="$paragraph" /> der Promotionsordnung der Juristischen und Wirtschaftswissenschaftlichen Fakultät der Martin-Luther-Universität zur Erlangung des Grades Doktor der Rechte (Doctor iuris, Dr. iur)
								</fo:block>
								<fo:block color="#000000" text-align="center" font-family="{$font}" font-size="12pt" font-weight="bold" padding-bottom="4em">
									Application for acceptance in the procedure for conferring a doctorate in accordance with § <xsl:value-of select="$paragraph" /> of the doctoral regulations of the Faculty of Law and Economics at Martin Luther University Halle-Wittenberg to obtain the degree Doctor of Law (Doctor iuris, Dr. iur)
								</fo:block>
							</xsl:when>
							<xsl:when test="contains($fakName,'Wirtschaftswiss. Bereich')">
								<fo:block color="#000000" text-align="center" font-family="{$font}" font-size="12pt" font-weight="bold">
									Antrag auf Zulassung zum Promotionsverfahren gemäß § <xsl:value-of select="$paragraph" /> der Promotionsordnung der Juristischen und Wirtschaftswissenschaftlichen Fakultät zur Erlangung des Grades Doktor der Wirtschaftswissenschaft (Doctor rerum politicarum, Dr. rer. pol.)
								</fo:block>
								<fo:block color="#000000" text-align="center" font-family="{$font}" font-size="12pt" font-weight="bold" padding-bottom="4em">
									Application for acceptance in the procedure for conferring a doctorate in accordance with § <xsl:value-of select="$paragraph" /> of the doctoral regulations of the Faculty of Law and Economics at Martin Luther University Halle-Wittenberg to obtain the degree Doctor of Economics (Doctor rerum politicarum, Dr. rer. pol.)
								</fo:block>
							</xsl:when>
							<xsl:otherwise>
								<fo:block color="#000000" text-align="center" font-family="{$font}" font-size="12pt" font-weight="bold">
									Antrag auf Zulassung zum Promotionsverfahren gemäß § <xsl:value-of select="$paragraph" /> der Promotionsordnung <xsl:value-of select="$artikel" /><xsl:text> </xsl:text><xsl:value-of select="$fakName" /> an der Martin-Luther-Universität Halle-Wittenberg
								</fo:block>
								<fo:block color="#000000" text-align="center" font-family="{$font}" font-size="12pt" font-weight="bold">
									Application for acceptance in the procedure for conferring a doctorate in accordance
								</fo:block>
								<fo:block color="#000000" text-align="center" font-family="{$font}" font-size="12pt" font-weight="bold" padding-bottom="4em">
									with § <xsl:value-of select="$paragraph" /> of the doctoral regulations of the <xsl:value-of select="$fakNameEn" /> at Martin Luther University Halle-Wittenberg
								</fo:block>
							</xsl:otherwise>
						</xsl:choose>
						<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="de" padding-bottom="1em">
							<fo:inline font-weight="bold">im Institut / at the Institute:</fo:inline><xsl:text>&#xA0;&#xA0;&#xA0;&#xA0;</xsl:text><xsl:value-of select="application/pluginReturn/fillingData/open_institute" />
						</fo:block>
						<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="de">
							In Kenntnis der Bestimmungen über die Voraussetzung und Verfahrensfragen zur Promotion beantrage ich hiermit die Eröffnung eines Promotionsverfahrens.
						</fo:block>
						<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="en" padding-bottom="2em">
							I hereby confirm acknowledgement of the formal regulations regarding the terms and conditions of the doctorate procedure and apply for the acceptance in the procedure for conferring a doctorate.
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block font-weight="bold">Persönliche Daten / Personal data:</fo:block>
							<fo:block>
								<fo:table table-layout="fixed" width="16.5cm">
									<fo:table-column column-width="8cm"/>
									<fo:table-column column-width="0.5cm"/>
									<fo:table-column column-width="8cm"/>
									<fo:table-body>
										<fo:table-row>
											<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_lastname" /><xsl:if test="open_birthname"><xsl:text>; geb. </xsl:text><xsl:value-of select="application/pluginReturn/fillingData/open_birthname" /></xsl:if></fo:block></fo:table-cell>
											<fo:table-cell><fo:block /></fo:table-cell>
											<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_firstname" /></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Name und Geburtsname / Surname and name at birth</xsl:text></fo:block></fo:table-cell>
											<fo:table-cell><fo:block  padding-bottom="1.5em" /></fo:table-cell>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Vorname / First name</xsl:text></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_dateOfBirth" /></fo:block></fo:table-cell>
											<fo:table-cell><fo:block /></fo:table-cell>
											<fo:table-cell>
												<fo:block>
													<xsl:value-of select="application/pluginReturn/fillingData/open_placeOfBirth" />
													<xsl:text>, </xsl:text>
													<xsl:for-each select="application/Countries/Country">
														<xsl:if test="akfz=/publishDetail/application/pluginReturn/fillingData/open_countryOfBirth">
															<xsl:value-of select="dtxt" />
														</xsl:if>
													</xsl:for-each>
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Geburtsdatum / Date of birth</xsl:text></fo:block></fo:table-cell>
											<fo:table-cell><fo:block  padding-bottom="1.5em" /></fo:table-cell>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Geburtsort, Land / Place of birth, Country</xsl:text></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell>
												<fo:block>
													<xsl:for-each select="application/Countries/Country">
														<xsl:if test="akfz=/publishDetail/application/pluginReturn/fillingData/open_national">
															<xsl:value-of select="dtxt" />
														</xsl:if>
													</xsl:for-each>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell><fo:block /></fo:table-cell>
											<fo:table-cell>
												<fo:block>
													<xsl:choose>
														<xsl:when test="application/pluginReturn/fillingData/open_gender='M'">männlich</xsl:when>
														<xsl:otherwise>weiblich</xsl:otherwise>
													</xsl:choose>
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Staatsangehörigkeit / Nationality</xsl:text></fo:block></fo:table-cell>
											<fo:table-cell><fo:block /></fo:table-cell>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Geschlecht / Gender</xsl:text></fo:block></fo:table-cell>
										</fo:table-row>
									</fo:table-body>
								</fo:table>
							</fo:block>
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block font-weight="bold">Wohnanschrift / Present address:</fo:block>
							<fo:block>
								<fo:table table-layout="fixed" width="16.5cm">
									<fo:table-column column-width="8cm"/>
									<fo:table-column column-width="0.5cm"/>
									<fo:table-column column-width="8cm"/>
									<fo:table-body>
										<fo:table-row>
											<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_street" /></fo:block></fo:table-cell>
											<fo:table-cell><fo:block /></fo:table-cell>
											<fo:table-cell>
												<fo:block>
													<xsl:value-of select="application/pluginReturn/fillingData/open_postcode" /><xsl:text> - </xsl:text><xsl:value-of select="application/pluginReturn/fillingData/open_city" />
													<xsl:text>, </xsl:text>
													<xsl:for-each select="application/Countries/Country">
														<xsl:if test="akfz=/publishDetail/application/pluginReturn/fillingData/open_country">
															<xsl:value-of select="dtxt" />
														</xsl:if>
													</xsl:for-each>
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Straße / Street</xsl:text></fo:block></fo:table-cell>
											<fo:table-cell><fo:block  padding-bottom="1.5em" /></fo:table-cell>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>PLZ – Ort, Land / Postalcode – City, Country</xsl:text></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_telefon" /></fo:block></fo:table-cell>
											<fo:table-cell><fo:block /></fo:table-cell>
											<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_email" /></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Telefonnummer / Telephone number  </xsl:text></fo:block></fo:table-cell>
											<fo:table-cell><fo:block /></fo:table-cell>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>E-Mail-Adresse / Email-address</xsl:text></fo:block></fo:table-cell>
										</fo:table-row>
									</fo:table-body>
								</fo:table>
							</fo:block>
						</fo:block>
						<xsl:if test="contains($fakName,'Medizinische')">
							<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
								<fo:block>
									<fo:table table-layout="fixed" width="16.5cm">
										<fo:table-column column-width="8cm"/>
										<fo:table-column column-width="0.5cm"/>
										<fo:table-column column-width="8cm"/>
										<fo:table-body>
											<fo:table-row>
												<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_degreeDate" /></fo:block></fo:table-cell>
												<fo:table-cell><fo:block /></fo:table-cell>
												<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_licenseDate" /></fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Datum des Hochschulabschlusses / Date of graduate degree</xsl:text></fo:block></fo:table-cell>
												<fo:table-cell><fo:block /></fo:table-cell>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>ggf. Datum des Approbation / date of license to practice medicine, if applicable</xsl:text></fo:block></fo:table-cell>
											</fo:table-row>
										</fo:table-body>
									</fo:table>
								</fo:block>
							</fo:block>
						</xsl:if>
						<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="de" padding-bottom="2em">
							<fo:table table-layout="fixed" width="16.5cm">
								<fo:table-column column-width="1.5cm"/>
								<fo:table-column column-width="15cm"/>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2"><fo:block font-weight="bold"><xsl:text>Aktueller Status der Antragstellerin bzw. des Antragsstellers / Current status of the applicant</xsl:text></fo:block></fo:table-cell>
									</fo:table-row>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Wissenschaftliche Mitarbeiterin bzw. Wissenschaftlicher Mitarbeiter der Universität Halle (Haushalt) / Scientific staff at the University of Halle (Budget)</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="application/pluginReturn/fillingData/open_appStatus = '1'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">0</xsl:with-param>
										<xsl:with-param name="additional">0</xsl:with-param>
									</xsl:call-template>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Wissenschaftliche Mitarbeiterin bzw. Wissenschaftlicher Mitarbeiter der Universität Halle (Drittmittel) / Scientific staff at the University of Halle (External funds)</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="application/pluginReturn/fillingData/open_appStatus = '2'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">0</xsl:with-param>
										<xsl:with-param name="additional">0</xsl:with-param>
									</xsl:call-template>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Stipendiatin bzw. Stipendiat / Scholarship holder</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="application/pluginReturn/fillingData/open_appStatus = '3'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">0</xsl:with-param>
										<xsl:with-param name="additional">0</xsl:with-param>
									</xsl:call-template>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Externe Doktorandin bzw. externer Doktorand/External doctoral candidate</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="application/pluginReturn/fillingData/open_appStatus = '4'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">0</xsl:with-param>
										<xsl:with-param name="additional">0</xsl:with-param>
									</xsl:call-template>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Anderes / Others</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="application/pluginReturn/fillingData/open_appStatus = '5'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">0</xsl:with-param>
										<xsl:with-param name="additional">0</xsl:with-param>
									</xsl:call-template>
								</fo:table-body>
							</fo:table>
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block font-weight="bold">Ein Arbeitsrechtsverhältnis besteht zur Zeit der Antragstellung mit / An employment contract for the applicant is currently in force with:</fo:block>
							<fo:block>
								<fo:table table-layout="fixed" width="16.5cm">
									<fo:table-column column-width="8cm"/>
									<fo:table-column column-width="0.5cm"/>
									<fo:table-column column-width="8cm"/>
									<fo:table-body>
										<fo:table-row>
											<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_buisnessInstitute" /></fo:block></fo:table-cell>
											<fo:table-cell><fo:block /></fo:table-cell>
											<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_buisnessAs" /></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Name der Institution / Name of institution</xsl:text></fo:block></fo:table-cell>
											<fo:table-cell><fo:block  padding-bottom="1.5em" /></fo:table-cell>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>als / as</xsl:text></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_buisnessStreet" /></fo:block></fo:table-cell>
											<fo:table-cell><fo:block /></fo:table-cell>
											<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_buisnessPostcode" /><xsl:text> - </xsl:text><xsl:value-of select="application/pluginReturn/fillingData/open_buisnessCity" /></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Straße / Street</xsl:text></fo:block></fo:table-cell>
											<fo:table-cell><fo:block  padding-bottom="1.5em" /></fo:table-cell>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>PLZ – Ort, Land / Postalcode – City, Country</xsl:text></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_buisnessTelefon" /></fo:block></fo:table-cell>
											<fo:table-cell><fo:block /></fo:table-cell>
											<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_buisnessEmail" /></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Telefonnummer / Telephone number  </xsl:text></fo:block></fo:table-cell>
											<fo:table-cell><fo:block /></fo:table-cell>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>E-Mail-Adresse / Email-address</xsl:text></fo:block></fo:table-cell>
										</fo:table-row>
									</fo:table-body>
								</fo:table>
							</fo:block>
						</fo:block>
					</fo:block-container>
					<fo:block break-after="page" />
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="de" padding-bottom="2em">
							<fo:table table-layout="fixed" width="16.5cm">
								<fo:table-column column-width="1.5cm"/>
								<fo:table-column column-width="15cm"/>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2"><fo:block>Die Erarbeitung der Dissertation erfolgte im Rahmen eines strukturierten Promotionsprogrammes. / The doctoral dissertation was undertaken within the framework of a structured doctoral program.</fo:block></fo:table-cell>
									</fo:table-row>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Ja / Yes</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/fillingData/open_prog) = 'j'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">gegebenenfalls Institution und Bezeichnung des Programmes / Institution and name of the program, if applicable:</xsl:with-param>
										<xsl:with-param name="additional"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/fillingData/open_progAdd) = ''">0</xsl:when><xsl:otherwise><xsl:value-of select="application/pluginReturn/fillingData/open_progAdd" /></xsl:otherwise></xsl:choose></xsl:with-param>
									</xsl:call-template>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Nein / No</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/fillingData/open_prog) = 'j'">0</xsl:when><xsl:otherwise>1</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">0</xsl:with-param>
										<xsl:with-param name="additional">0</xsl:with-param>
									</xsl:call-template>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block-container>
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="de" padding-bottom="2em">
							<fo:table table-layout="fixed" width="16.5cm">
								<fo:table-column column-width="1.5cm"/>
								<fo:table-column column-width="15cm"/>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2"><fo:block>Die Erarbeitung der Dissertation erfolgte im Rahmen eines bi-nationalen Promotionsverfahrens (Cotutelle de thèse). / The doctoral dissertation was undertaken within the framework of a double doctorate (Cotutelle de thèse).</fo:block></fo:table-cell>
									</fo:table-row>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Ja / Yes</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/fillingData/open_binat) = 'j'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">gegebenenfalls Institution und Land / Institution und country, if applicable:</xsl:with-param>
										<xsl:with-param name="additional"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/fillingData/open_binatAdd) = ''">0</xsl:when><xsl:otherwise><xsl:value-of select="application/pluginReturn/fillingData/open_binatAdd" /></xsl:otherwise></xsl:choose></xsl:with-param>
									</xsl:call-template>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Nein / No</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/fillingData/open_binat) = 'j'">0</xsl:when><xsl:otherwise>1</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">0</xsl:with-param>
										<xsl:with-param name="additional">0</xsl:with-param>
									</xsl:call-template>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block-container>
					<xsl:if test="not(contains($fakName,'Medizinische'))">
						<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
							<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="de" padding-bottom="2em">
								<fo:table table-layout="fixed" width="16.5cm">
									<fo:table-column column-width="1.5cm"/>
									<fo:table-column column-width="15cm"/>
									<fo:table-body>
										<fo:table-row>
											<fo:table-cell number-columns-spanned="2"><fo:block>Handelt es sich um eine kumulative Dissertation? / Is this a cumulative dissertation?</fo:block></fo:table-cell>
										</fo:table-row>
										<xsl:call-template name="checkField">
											<xsl:with-param name="text">Ja / Yes</xsl:with-param>
											<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/fillingData/open_promoType) = 'j'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
											<xsl:with-param name="additionalPre">0</xsl:with-param>
											<xsl:with-param name="additional">0</xsl:with-param>
										</xsl:call-template>
										<xsl:call-template name="checkField">
											<xsl:with-param name="text">Nein / No</xsl:with-param>
											<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/fillingData/open_promoType) = 'j'">0</xsl:when><xsl:otherwise>1</xsl:otherwise></xsl:choose></xsl:with-param>
											<xsl:with-param name="additionalPre">0</xsl:with-param>
											<xsl:with-param name="additional">0</xsl:with-param>
										</xsl:call-template>
									</fo:table-body>
								</fo:table>
							</fo:block>
						</fo:block-container>
					</xsl:if>
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de">
							<fo:block>Die Bestätigung zur Annahme als Doktorandin bzw. Doktorand erfolgte am <xsl:value-of select="application/pluginReturn/fillingData/open_assumption" />. </fo:block>
						</fo:block>
					</fo:block-container>
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block>The confirmation for acceptance as a doctoral candidate dated by <xsl:value-of select="application/pluginReturn/fillingData/open_assumption" />. </fo:block>
						</fo:block>
					</fo:block-container>
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block font-weight="bold">Fachgebiet der angestrebten Promotion / Field of intended doctorate (gemäß Fächerkatalog der Promotionsordnung der Einrichtung):</fo:block>
							<fo:block><xsl:value-of select="$fachGebiet" /></fo:block>
						</fo:block>
					</fo:block-container>
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block font-weight="bold">Angestrebter akademischer Grad / Intended academic degree:</fo:block>
							<fo:block>
								<xsl:for-each select="application/AkadGradesForPromotion/AkadGrade">
									<xsl:if test="gradid = /publishDetail/application/pluginReturn/fillingData/open_gettingGrade">
										<xsl:value-of select="ltxt" /> (<xsl:value-of select="dtxt" />)
									</xsl:if>
								</xsl:for-each>
							</fo:block>
						</fo:block>
					</fo:block-container>
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block font-weight="bold"><xsl:text>Titel der Dissertation / Title of the dissertation: </xsl:text></fo:block>
							<fo:block padding-bottom="1em"><xsl:value-of select="application/pluginReturn/fillingData/open_title" /></fo:block>
						</fo:block>
					</fo:block-container>
					<xsl:if test="contains($fakName,'Medizinische')">
						<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
							<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="de" padding-bottom="2em">
								<fo:table table-layout="fixed" width="16.5cm">
									<fo:table-column column-width="1.5cm"/>
									<fo:table-column column-width="15cm"/>
									<fo:table-body>
										<fo:table-row>
											<fo:table-cell number-columns-spanned="2"><fo:block font-weight="bold"><xsl:text>Form der Dissertation / Kind of dissertation</xsl:text></fo:block></fo:table-cell>
										</fo:table-row>
										<xsl:for-each select="/publishDetail/application/DissTypes/DissType">
											<xsl:call-template name="checkField">
												<xsl:with-param name="text"><xsl:value-of select="dtxt"/></xsl:with-param>
												<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="/publishDetail/application/pluginReturn/fillingData/open_typeOfDissertation = id">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
												<xsl:with-param name="additionalPre">0</xsl:with-param>
												<xsl:with-param name="additional">0</xsl:with-param>
											</xsl:call-template>
										</xsl:for-each>
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
											<fo:table-cell number-columns-spanned="2"><fo:block>Beurteilung durch die Ethik-Kommission notwendig? / Evaluation by the ethical review committee required?</fo:block></fo:table-cell>
										</fo:table-row>
										<xsl:call-template name="checkField">
											<xsl:with-param name="text">Ja / Yes</xsl:with-param>
											<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/fillingData/open_ethical) = 'j'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
											<xsl:with-param name="additionalPre">Datum der Beurteilung / Date of the evaluation</xsl:with-param>
											<xsl:with-param name="additional"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/fillingData/open_ethicalAdd) = ''">0</xsl:when><xsl:when test="normalize-space(application/pluginReturn/fillingData/open_ethical) != 'j'">0</xsl:when><xsl:otherwise><xsl:value-of select="application/pluginReturn/fillingData/open_ethicalAdd" /></xsl:otherwise></xsl:choose></xsl:with-param>
										</xsl:call-template>
										<xsl:call-template name="checkField">
											<xsl:with-param name="text">Nein / No</xsl:with-param>
											<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/fillingData/open_ethical) = 'j'">0</xsl:when><xsl:otherwise>1</xsl:otherwise></xsl:choose></xsl:with-param>
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
											<fo:table-cell number-columns-spanned="2"><fo:block>Teilnahme Graduiertenkolleg / Participation on research training group</fo:block></fo:table-cell>
										</fo:table-row>
										<xsl:call-template name="checkField">
											<xsl:with-param name="text">Ja / Yes</xsl:with-param>
											<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/fillingData/open_resTrGroup) = 'j'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">Name des Kollegs / Name of the group:</xsl:with-param>
										<xsl:with-param name="additional"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/fillingData/open_resTrGroupAdd) = ''">0</xsl:when><xsl:when test="normalize-space(application/pluginReturn/fillingData/open_resTrGroup) != 'j'">0</xsl:when><xsl:otherwise><xsl:value-of select="application/pluginReturn/fillingData/open_resTrGroupAdd" /></xsl:otherwise></xsl:choose></xsl:with-param>
										</xsl:call-template>
										<xsl:call-template name="checkField">
											<xsl:with-param name="text">Nein / No</xsl:with-param>
											<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/fillingData/open_resTrGroup) = 'j'">0</xsl:when><xsl:otherwise>1</xsl:otherwise></xsl:choose></xsl:with-param>
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
											<fo:table-cell number-columns-spanned="2"><fo:block>Statistische Beratung geplant / statistical consulting intended</fo:block></fo:table-cell>
										</fo:table-row>
										<xsl:call-template name="checkField">
											<xsl:with-param name="text">Ja / Yes</xsl:with-param>
											<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/fillingData/open_statistic) = 'j'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">Name der Einrichtung / Name of the institution:</xsl:with-param>
										<xsl:with-param name="additional"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/fillingData/open_statisticAdd) = ''">0</xsl:when><xsl:when test="normalize-space(application/pluginReturn/fillingData/open_statistic) != 'j'">0</xsl:when><xsl:otherwise><xsl:value-of select="application/pluginReturn/fillingData/open_statisticAdd" /></xsl:otherwise></xsl:choose></xsl:with-param>
										</xsl:call-template>
										<xsl:call-template name="checkField">
											<xsl:with-param name="text">Nein / No</xsl:with-param>
											<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/fillingData/open_statistic) = 'j'">0</xsl:when><xsl:otherwise>1</xsl:otherwise></xsl:choose></xsl:with-param>
											<xsl:with-param name="additionalPre">0</xsl:with-param>
											<xsl:with-param name="additional">0</xsl:with-param>
										</xsl:call-template>
									</fo:table-body>
								</fo:table>
							</fo:block>
						</fo:block-container>
					</xsl:if>
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de">
							<fo:table table-layout="fixed" width="16.5cm">
								<fo:table-column column-width="16.5cm"/>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell><fo:block font-weight="bold">Betreuende Hochschullehrerin bzw. betreuender Hochschullehrer / Mentoring professor (1):</fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_tutor1Name" /></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt" padding-bottom="1em">Akad. Titel, akad. Grad, Name, Vorname / Academic title, degree, surname, first name</fo:block></fo:table-cell>
									</fo:table-row>
									<xsl:if test="normalize-space(application/pluginReturn/fillingData/open_tutor2Name) != ''">
										<fo:table-row>
											<fo:table-cell padding-top="1em"><fo:block font-weight="bold">Betreuende Hochschullehrerin bzw. betreuender Hochschullehrer / Mentoring professor (2):</fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_tutor2Name" /></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt" padding-bottom="1em">Akad. Titel, akad. Grad, Name, Vorname / Academic title, degree, surname, first name</fo:block></fo:table-cell>
										</fo:table-row>
									</xsl:if>
									<xsl:if test="normalize-space(application/pluginReturn/fillingData/open_tutor3Name) != ''">
										<fo:table-row>
											<fo:table-cell padding-top="1em"><fo:block font-weight="bold">Betreuende Hochschullehrerin bzw. betreuender Hochschullehrer / Mentoring professor (3):</fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_tutor3Name" /></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt" padding-bottom="1em">Akad. Titel, akad. Grad, Name, Vorname / Academic title, degree, surname, first name</fo:block></fo:table-cell>
										</fo:table-row>
									</xsl:if>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block-container>
					<xsl:if test="normalize-space(application/pluginReturn/fillingData/open_eval1Name) != ''">
						<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
							<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-top="2em">
								<fo:table table-layout="fixed" width="16.5cm">
									<fo:table-column column-width="16.5cm"/>
									<fo:table-body>
											<fo:table-row>
												<fo:table-cell padding-top="1em"><fo:block font-weight="bold">Vorschlag für Gutachter / Suggested reviewer (1):</fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_eval1Name" /></fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt" padding-bottom="1em">Akad. Titel, akad. Grad, Name, Vorname / Academic title, degree, surname, first name</fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_eval1Institute" /></fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt">Wissenschaftl. Einrichtung, Adresse / Sientific institution, address</fo:block></fo:table-cell>
											</fo:table-row>
									</fo:table-body>
								</fo:table>
							</fo:block>
						</fo:block-container>
					</xsl:if>
					<xsl:if test="normalize-space(application/pluginReturn/fillingData/open_eval2Name) != ''">
						<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
							<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de">
								<fo:table table-layout="fixed" width="16.5cm">
									<fo:table-column column-width="16.5cm"/>
									<fo:table-body>
											<fo:table-row>
												<fo:table-cell padding-top="1em"><fo:block font-weight="bold">Vorschlag für Gutachter / Suggested reviewer (2):</fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_eval2Name" /></fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt" padding-bottom="1em">Akad. Titel, akad. Grad, Name, Vorname / Academic title, degree, surname, first name</fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_eval2Institute" /></fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt">Wissenschaftl. Einrichtung, Adresse / Sientific institution, address</fo:block></fo:table-cell>
											</fo:table-row>
									</fo:table-body>
								</fo:table>
							</fo:block>
						</fo:block-container>
					</xsl:if>
					<xsl:if test="normalize-space(application/pluginReturn/fillingData/open_eval3Name) != ''">
						<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
							<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de">
								<fo:table table-layout="fixed" width="16.5cm">
									<fo:table-column column-width="16.5cm"/>
									<fo:table-body>
											<fo:table-row>
												<fo:table-cell padding-top="1em"><fo:block font-weight="bold">Vorschlag für Gutachter / Suggested reviewer (3):</fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_eval3Name" /></fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt" padding-bottom="1em">Akad. Titel, akad. Grad, Name, Vorname / Academic title, degree, surname, first name</fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_eval3Institute" /></fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt">Wissenschaftl. Einrichtung, Adresse / Sientific institution, address</fo:block></fo:table-cell>
											</fo:table-row>
									</fo:table-body>
								</fo:table>
							</fo:block>
						</fo:block-container>
					</xsl:if>
					<xsl:if test="normalize-space(application/pluginReturn/fillingData/open_eval4Name) != ''">
						<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
							<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de">
								<fo:table table-layout="fixed" width="16.5cm">
									<fo:table-column column-width="16.5cm"/>
									<fo:table-body>
											<fo:table-row>
												<fo:table-cell padding-top="1em"><fo:block font-weight="bold">Vorschlag für Gutachter / Suggested reviewer (4):</fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_eval4Name" /></fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt" padding-bottom="1em">Akad. Titel, akad. Grad, Name, Vorname / Academic title, degree, surname, first name</fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_eval4Institute" /></fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt">Wissenschaftl. Einrichtung, Adresse / Sientific institution, address</fo:block></fo:table-cell>
											</fo:table-row>
									</fo:table-body>
								</fo:table>
							</fo:block>
						</fo:block-container>
					</xsl:if>
					<xsl:if test="normalize-space(application/pluginReturn/fillingData/open_kommisName) != ''">
						<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
							<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-top="2em">
								<fo:table table-layout="fixed" width="16.5cm">
									<fo:table-column column-width="16.5cm"/>
									<fo:table-body>
											<fo:table-row>
												<fo:table-cell padding-top="1em"><fo:block font-weight="bold">Vorschlag für das Wahlmitglied der Prüfungskommission / Suggested electoral member of the examination board:</fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_kommisName" /></fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt" padding-bottom="1em">Akad. Titel, akad. Grad, Name, Vorname / Academic title, degree, surname, first name</fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_kommisInstitute" /></fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt">Wissenschaftl. Einrichtung, Adresse / Sientific institution, address</fo:block></fo:table-cell>
											</fo:table-row>
									</fo:table-body>
								</fo:table>
							</fo:block>
						</fo:block-container>
					</xsl:if>
					<xsl:if test="normalize-space(application/pluginReturn/fillingData/open_kommis1Name) != ''">
						<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
							<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-top="2em">
								<fo:table table-layout="fixed" width="16.5cm">
									<fo:table-column column-width="16.5cm"/>
									<fo:table-body>
											<fo:table-row>
												<fo:table-cell padding-top="1em"><fo:block font-weight="bold">Vorschlag für das Wahlmitglied der Prüfungskommission / Suggested electoral member of the examination board (1):</fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_kommis1Name" /></fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt" padding-bottom="1em">Akad. Titel, akad. Grad, Name, Vorname / Academic title, degree, surname, first name</fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_kommis1Institute" /></fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt">Wissenschaftl. Einrichtung, Adresse / Sientific institution, address</fo:block></fo:table-cell>
											</fo:table-row>
									</fo:table-body>
								</fo:table>
							</fo:block>
						</fo:block-container>
					</xsl:if>
					<xsl:if test="normalize-space(application/pluginReturn/fillingData/open_kommis2Name) != ''">
						<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
							<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de">
								<fo:table table-layout="fixed" width="16.5cm">
									<fo:table-column column-width="16.5cm"/>
									<fo:table-body>
											<fo:table-row>
												<fo:table-cell padding-top="1em"><fo:block font-weight="bold">Vorschlag für das Wahlmitglied der Prüfungskommission / Suggested electoral member of the examination board (2):</fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_kommis2Name" /></fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt" padding-bottom="1em">Akad. Titel, akad. Grad, Name, Vorname / Academic title, degree, surname, first name:</fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_kommis2Institute" /></fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt">Wissenschaftl. Einrichtung, Adresse / Sientific institution, address:</fo:block></fo:table-cell>
											</fo:table-row>
									</fo:table-body>
								</fo:table>
							</fo:block>
						</fo:block-container>
					</xsl:if>
					<xsl:if test="normalize-space(application/pluginReturn/fillingData/open_kommis3Name) != ''">
						<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
							<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de">
								<fo:table table-layout="fixed" width="16.5cm">
									<fo:table-column column-width="16.5cm"/>
									<fo:table-body>
											<fo:table-row>
												<fo:table-cell padding-top="1em"><fo:block font-weight="bold">Vorschlag für das Wahlmitglied der Prüfungskommission / Suggested electoral member of the examination board (3):</fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_kommis3Name" /></fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt" padding-bottom="1em">Akad. Titel, akad. Grad, Name, Vorname / Academic title, degree, surname, first name:</fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/fillingData/open_kommis3Institute" /></fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt">Wissenschaftl. Einrichtung, Adresse / Sientific institution, address:</fo:block></fo:table-cell>
											</fo:table-row>
									</fo:table-body>
								</fo:table>
							</fo:block>
						</fo:block-container>
					</xsl:if>
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-top="2em">
							<xsl:choose>
								<xsl:when test="contains($fakName,'Theol')">Kenntnisnahme der betreuenden Hochschullehrerin bzw. des betreuenden Hochschullehrers <xsl:value-of select="$artikel" /><xsl:text> </xsl:text><xsl:value-of select="$fakName" /> / Confirmation of the mentoring professor of the <xsl:value-of select="$fakNameEn" /> (1)</xsl:when>
								<xsl:otherwise>Kenntnisnahme und Bestätigung der Bereitschaft der betreuenden Hochschullehrerin bzw. des betreuenden Hochschullehrers / Confirmation of willingness of the mentoring professor (1)</xsl:otherwise>
							</xsl:choose>
						</fo:block>
						<fo:block font-size="8pt" padding-top="6em" padding-bottom="1em">
							<fo:table table-layout="fixed" width="16.5cm">
								<fo:table-column column-width="7cm"/>
								<fo:table-column column-width="0.5cm"/>
								<fo:table-column column-width="9cm"/>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Datum / Date</xsl:text></fo:block></fo:table-cell>
										<fo:table-cell><fo:block  padding-bottom="1.5em" /></fo:table-cell>
										<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Unterschrift der Betreuerin bzw. des Betreuers / Signature of the mentor (1)</xsl:text></fo:block></fo:table-cell>
									</fo:table-row>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block-container>
					<xsl:if test="normalize-space(application/pluginReturn/fillingData/open_tutor2Name) != ''">
						<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
							<fo:block hyphenate="true" language="de">
								<xsl:choose>
									<xsl:when test="contains($fakName,'Theol')">Kenntnisnahme der betreuenden Hochschullehrerin bzw. des betreuenden Hochschullehrers (2) / Confirmation of the mentoring professor (2)</xsl:when>
									<xsl:otherwise>Kenntnisnahme und Bestätigung der Bereitschaft der betreuenden Hochschullehrerin bzw. des betreuenden Hochschullehrers / Confirmation of willingness of the mentoring professor (2)</xsl:otherwise>
								</xsl:choose>
							</fo:block>
							<fo:block font-size="8pt" padding-top="6em" padding-bottom="1em">
								<fo:table table-layout="fixed" width="16.5cm">
									<fo:table-column column-width="7cm"/>
									<fo:table-column column-width="0.5cm"/>
									<fo:table-column column-width="9cm"/>
									<fo:table-body>
										<fo:table-row>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Datum / Date</xsl:text></fo:block></fo:table-cell>
											<fo:table-cell><fo:block  padding-bottom="1.5em" /></fo:table-cell>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Unterschrift der Betreuerin bzw. des Betreuers / Signature of the mentor (2)</xsl:text></fo:block></fo:table-cell>
										</fo:table-row>
									</fo:table-body>
								</fo:table>
							</fo:block>
						</fo:block-container>
					</xsl:if>
					<xsl:if test="normalize-space(application/pluginReturn/fillingData/open_tutor3Name) != ''">
						<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
							<fo:block hyphenate="true" language="de">
								<xsl:choose>
									<xsl:when test="contains($fakName,'Theol')">Kenntnisnahme der betreuenden Hochschullehrerin bzw. des betreuenden Hochschullehrers (3) / Confirmation of the mentoring professor (3)</xsl:when>
									<xsl:otherwise>Kenntnisnahme und Bestätigung der Bereitschaft der betreuenden Hochschullehrerin bzw. des betreuenden Hochschullehrers / Confirmation of willingness of the mentoring professor (3)</xsl:otherwise>
								</xsl:choose>
							</fo:block>
							<fo:block font-size="8pt" padding-top="6em" padding-bottom="1em">
								<fo:table table-layout="fixed" width="16.5cm">
									<fo:table-column column-width="7cm"/>
									<fo:table-column column-width="0.5cm"/>
									<fo:table-column column-width="9cm"/>
									<fo:table-body>
										<fo:table-row>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Datum / Date</xsl:text></fo:block></fo:table-cell>
											<fo:table-cell><fo:block  padding-bottom="1.5em" /></fo:table-cell>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Unterschrift der Betreuerin bzw. des Betreuers / Signature of the mentor (3)</xsl:text></fo:block></fo:table-cell>
										</fo:table-row>
									</fo:table-body>
								</fo:table>
							</fo:block>
						</fo:block-container>
					</xsl:if>
					<xsl:if test="contains($fakName,'Medizin')">
						<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
							<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de">
								<fo:block>Kenntnisnahme der Leiterin bzw. des Leiters des betreffenden Instituts bzw. der betreffenden Klinik / Confirmation by the head of the relevant institute, respectively relevant clinic</fo:block>
								<fo:block font-size="8pt" padding-top="6em">
									<fo:table>
										<fo:table-column column-width="7cm"/>
										<fo:table-column column-width="0.5cm"/>
										<fo:table-column column-width="9cm"/>
										<fo:table-body>
											<fo:table-row>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Datum / Date</xsl:text></fo:block></fo:table-cell>
												<fo:table-cell><fo:block  padding-bottom="1.5em" /></fo:table-cell>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Unterschrift der Leiterin bzw. des Leiters / Signature of the head</xsl:text></fo:block></fo:table-cell>
											</fo:table-row>
										</fo:table-body>
									</fo:table>
								</fo:block>
							</fo:block>
						</fo:block-container>
					</xsl:if>
					<xsl:if test="contains($fakName,'Medizinische')">
						<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
							<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-top="2em" padding-bottom="2em">
								<fo:block font-weight="bold">Erklärungen / Declarations</fo:block>
								<fo:block>Ich erklären, dass ich mich an keiner anderen Hochschule einem Promotionsverfahren unterzogen bzw. eine Promotion begonnen habe. / I declare that I have not completed or initiated a doctorate procedure at any other university.</fo:block>
								<fo:block font-size="8pt" padding-top="6em">
									<fo:table table-layout="fixed" width="16.5cm">
										<fo:table-column column-width="7cm"/>
										<fo:table-column column-width="0.5cm"/>
										<fo:table-column column-width="9cm"/>
										<fo:table-body>
											<fo:table-row>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Ort / Place, Datum / Date</xsl:text></fo:block></fo:table-cell>
												<fo:table-cell><fo:block  padding-bottom="1.5em" /></fo:table-cell>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Unterschrift der Antragstellerin bzw. des Antragstellers / Signature of the applicant</xsl:text></fo:block></fo:table-cell>
											</fo:table-row>
										</fo:table-body>
									</fo:table>
								</fo:block>
							</fo:block>
						</fo:block-container>
					</xsl:if>
					<xsl:choose>
						<xsl:when test="contains($fakName,'Theol')">
							<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
								<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-top="2em" padding-bottom="2em">
									<fo:block font-weight="bold">Erklärung / Declaration</fo:block>
									<fo:block>Ich erkläre an Eides statt, dass ich die vorliegende Arbeit an keiner anderen wissenschaftlichen Einrichtung zur Erlangung eines akademischen Grades eingereicht habe und mich an keiner anderen Hochschule in einem Promotionsverfahren zum Dr. theol. befinde. Ich erkläre an Eides statt, dass ich die vorliegende Arbeit selbstständig und ohne fremde Hilfe verfasst, keine anderen als die von mir angegebenen Quellen und Hilfsmittel benutzt und die den benutzten Werken wörtlich oder inhaltlich entnommenen Stellen als solche kenntlich gemacht habe. / I affirm in lieu of an oath that I didn't submit the present thesis at any other academic institution with the objective of receiving a degree and that I'm not accepted in the procedure for conferring a doctorate at any other university. I affirm in lieu of an oath that the present thesis is entirely my own work and was written without assistance. I used only the cited sources and included all the citations correctly both in word or content.</fo:block>
									<fo:block font-size="8pt" padding-top="6em">
										<fo:table table-layout="fixed" width="16.5cm">
											<fo:table-column column-width="7cm"/>
											<fo:table-column column-width="0.5cm"/>
											<fo:table-column column-width="9cm"/>
											<fo:table-body>
												<fo:table-row>
													<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Ort / Place, Datum / Date</xsl:text></fo:block></fo:table-cell>
													<fo:table-cell><fo:block  padding-bottom="1.5em" /></fo:table-cell>
													<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Unterschrift der Antragstellerin bzw. des Antragstellers / Signature of the applicant</xsl:text></fo:block></fo:table-cell>
												</fo:table-row>
											</fo:table-body>
										</fo:table>
									</fo:block>
								</fo:block>
							</fo:block-container>
						</xsl:when>
						<xsl:otherwise>
							<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
								<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-top="2em" padding-bottom="2em">
									<fo:block font-weight="bold">Erklärung zum Wahrheitsgehalt der Angaben / Declaration concerning the truth of information given</fo:block>
									<fo:block>Ich erkläre, die Angaben wahrheitsgemäß gemacht und die wissenschaftliche Arbeit an keiner anderen wissenschaftlichen Einrichtung zur Erlangung eines akademischen Grades eingereicht zu haben. / I declare that all information given is accurate and complete. The thesis has not been used previously at this or any other university in order to achieve an academic degree.</fo:block>
									<fo:block font-size="8pt" padding-top="6em">
										<fo:table table-layout="fixed" width="16.5cm">
											<fo:table-column column-width="7cm"/>
											<fo:table-column column-width="0.5cm"/>
											<fo:table-column column-width="9cm"/>
											<fo:table-body>
												<fo:table-row>
													<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Ort / Place, Datum / Date</xsl:text></fo:block></fo:table-cell>
													<fo:table-cell><fo:block  padding-bottom="1.5em" /></fo:table-cell>
													<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Unterschrift der Antragstellerin bzw. des Antragstellers / Signature of the applicant</xsl:text></fo:block></fo:table-cell>
												</fo:table-row>
											</fo:table-body>
										</fo:table>
									</fo:block>
								</fo:block>
							</fo:block-container>
							<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
								<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
									<fo:block font-weight="bold">Eidesstattliche Erklärung / Declaration under oath</fo:block>
									<fo:block>Ich erkläre an Eides statt, dass ich die Arbeit selbstständig und ohne fremde Hilfe verfasst, keine anderen als die von mir angegebenen Quellen und Hilfsmittel benutzt und die den benutzten Werken wörtlich oder inhaltlich entnommenen Stellen als solche kenntlich gemacht habe. / I declare under oath that this thesis is my own work entirely and has been written without any help from other people. I used only the sources mentioned and included all the citations correctly both in word or content.</fo:block>
									<fo:block font-size="8pt" padding-top="6em">
										<fo:table table-layout="fixed" width="16.5cm">
											<fo:table-column column-width="7cm"/>
											<fo:table-column column-width="0.5cm"/>
											<fo:table-column column-width="9cm"/>
											<fo:table-body>
												<fo:table-row>
													<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Ort / Place, Datum / Date</xsl:text></fo:block></fo:table-cell>
													<fo:table-cell><fo:block  padding-bottom="1.5em" /></fo:table-cell>
													<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Unterschrift der Antragstellerin bzw. des Antragstellers / Signature of the applicant</xsl:text></fo:block></fo:table-cell>
												</fo:table-row>
											</fo:table-body>
										</fo:table>
									</fo:block>
								</fo:block>
							</fo:block-container>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:if test="not(contains($fakName,'Theol')) and not(contains($fakName,'Juristischer Bereich'))  and not(contains($fakName,'Medizin')) ">
						<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
							<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
								<fo:block font-weight="bold">Vorstrafen, Ermittlungsverfahren / Preliminary investigations</fo:block>
								<fo:block>Hiermit erkläre ich, dass ich weder vorbestraft bin noch dass gegen mich Ermittlungsverfahren anhängig sind. / I hereby declare that I have no criminal record and that no preliminary investigations are pending against me.</fo:block>
								<fo:block font-size="8pt" padding-top="6em">
									<fo:table table-layout="fixed" width="16.5cm">
										<fo:table-column column-width="7cm"/>
										<fo:table-column column-width="0.5cm"/>
										<fo:table-column column-width="9cm"/>
										<fo:table-body>
											<fo:table-row>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Ort / Place, Datum / Date</xsl:text></fo:block></fo:table-cell>
												<fo:table-cell><fo:block  padding-bottom="1.5em" /></fo:table-cell>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Unterschrift der Antragstellerin bzw. des Antragstellers / Signature of the applicant</xsl:text></fo:block></fo:table-cell>
											</fo:table-row>
										</fo:table-body>
									</fo:table>
								</fo:block>
							</fo:block>
						</fo:block-container>
					</xsl:if>
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block font-weight="bold">Anlagen / Enclosures</fo:block>
							<fo:block>Anlagen entsprechend § <xsl:value-of select="$paragraph" /> der Promotionsordnung <xsl:value-of select="$artikel" /><xsl:text> </xsl:text><xsl:value-of select="$fakName" /> an der Martin-Luther-Universität / Enclosures in accordance with § <xsl:value-of select="$paragraph" /> of the  doctoral regulations of the <xsl:value-of select="$fakNameEn" /> at MartinLuther University Halle-Wittenberg.</fo:block>
							<xsl:if test="contains($fakName,'Medizinische')">
								<fo:list-block>
									<fo:list-item>
										<fo:list-item-label end-indent="label-end()"><fo:block>1.</fo:block></fo:list-item-label>
										<fo:list-item-body start-indent="body-start()">
											<fo:block>
												Nachweis über den erfolgreichen Abschluss des Hochschulstudiums entsprechend §3 Abs. 1 oder 3, soweit Unterlagen nicht schon mit einem Antrag auf Annahme als Doktiandin bzw. Doktorand (§4 Abs. 2) eingereicht worden sind. / Evidence concerning the successful completion of a University study under $3, paragraphs 1, 3 or 4, insofar as such documents were not already submitted with application for acceptance as a doctoral candidate.
											</fo:block>
										</fo:list-item-body>
									</fo:list-item>
									<fo:list-item>
										<fo:list-item-label end-indent="label-end()"><fo:block>2.</fo:block></fo:list-item-label>
										<fo:list-item-body start-indent="body-start()">
											<fo:block>
												Gegebenenfalls Publikationsliste / List of publications, if applicable
											</fo:block>
										</fo:list-item-body>
									</fo:list-item>
									<fo:list-item>
										<fo:list-item-label end-indent="label-end()"><fo:block>3.</fo:block></fo:list-item-label>
										<fo:list-item-body start-indent="body-start()">
											<fo:block>
												Gegebenenfalls Leistungsnachweise über ein erfolgreich absolviertes promotionsbegleitetes Studienprogramm. / Certificates of a successfully completed doctoral study programm, if applicable.
											</fo:block>
										</fo:list-item-body>
									</fo:list-item>
								</fo:list-block>							
							</xsl:if>
						</fo:block>
					</fo:block-container>
					<fo:block break-after="page" />
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block font-weight="bold">Bearbeitungsbogen <xsl:value-of select="$artikel" /><xsl:text> </xsl:text><xsl:value-of select="$fakName" /> / Editing form of the <xsl:value-of select="$fakNameEn" /></fo:block>
							<fo:block>(Wird von <xsl:value-of select="$artikel" /><xsl:text> </xsl:text><xsl:value-of select="$fakName" /> ausgefüllt / To be filled in by the <xsl:value-of select="$fakNameEn" />)</fo:block>
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block>Die gemäß der Promotionsordnung <xsl:value-of select="$artikel" /><xsl:text> </xsl:text><xsl:value-of select="$fakName" /> der Martin-Luther-Universität Halle-Wittenberg einzureichenden Unterlagen wurden vollzählig und ordnungsgemäß vorgelegt.</fo:block>
						</fo:block>
						<xsl:variable name="annahmeParagraph">
							<xsl:choose>
								<xsl:when test="contains($fakName,'Wirtschaftswiss. Bereich')">5 und</xsl:when>
								<xsl:otherwise>4 Abs. 2</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<xsl:variable name="ablehungParagraph">
							<xsl:choose>
								<xsl:when test="contains($fakName,'Wirtschaftswiss. Bereich')">7 Abs. 6</xsl:when>
								<xsl:otherwise>6 Abs. 2</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<xsl:choose>
								<xsl:when test="contains($fakName,'Medizinische')">
									<fo:block>Die Annahme als Doktorandin bzw. Doktorand erfolgte gemäß § <xsl:value-of select="$annahmeParagraph" /> mindestens sechs Monate vor dem Antrag auf Zulassung zum Promotionsverfahren.</fo:block>
								</xsl:when>
								<xsl:otherwise>
									<fo:block>Die Annahme als Doktorandin bzw. Doktorand erfolgte gemäß § <xsl:value-of select="$annahmeParagraph" /> mindestens ein Jahr vor dem Antrag auf Zulassung zum Promotionsverfahren.</fo:block>
								</xsl:otherwise>
							</xsl:choose>
							<fo:table table-layout="fixed" width="16.5cm">
								<fo:table-column column-width="1.5cm"/>
								<fo:table-column column-width="15cm"/>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell><fo:block text-align="center"><fo:inline font-family="ZapfDingbats">&#x274F;</fo:inline></fo:block></fo:table-cell>
										<fo:table-cell><fo:block>Ja</fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell><fo:block text-align="center"><fo:inline font-family="ZapfDingbats">&#x274F;</fo:inline></fo:block></fo:table-cell>
										<fo:table-cell><fo:block>Nein</fo:block></fo:table-cell>
									</fo:table-row>
								</fo:table-body>
							</fo:table>
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block font-size="8pt" padding-top="6em">
								<fo:table table-layout="fixed" width="16.5cm">
									<fo:table-column column-width="7cm"/>
									<fo:table-column column-width="0.5cm"/>
									<fo:table-column column-width="9cm"/>
									<fo:table-body>
										<fo:table-row>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Ort, Datum</xsl:text></fo:block></fo:table-cell>
											<fo:table-cell><fo:block  padding-bottom="1.5em" /></fo:table-cell>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Unterschrift der Beauftragten bzw. des Beauftragten der Dekanin bzw. des Dekans</xsl:text></fo:block></fo:table-cell>
										</fo:table-row>
									</fo:table-body>
								</fo:table>
							</fo:block>
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block>Dem Antrag auf Zulassung zum Promotionsverfahrens zur Erlangung des akademischen Grades eines <xsl:value-of select="$gradNameForDoc" /> für das Wissenschaftsgebiet <xsl:value-of select="$fachGebiet" /> wird entsprochen.</fo:block>
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block>Im Fall der Zurückweisung nach § <xsl:value-of select="$ablehungParagraph" /> der Promotionsordnung <xsl:value-of select="$artikel" /><xsl:text> </xsl:text><xsl:value-of select="$fakName" /> liegen dem Schreiben an die Doktorandin bzw. den Doktoranden eine Begründung und Rechtsbehelfsbelehrung bei.</fo:block>
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block font-size="8pt" padding-top="6em">
								<fo:table table-layout="fixed" width="16.5cm">
									<fo:table-column column-width="7cm"/>
									<fo:table-column column-width="0.5cm"/>
									<fo:table-column column-width="9cm"/>
									<fo:table-body>
										<fo:table-row>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Ort, Datum</xsl:text></fo:block></fo:table-cell>
											<fo:table-cell><fo:block  padding-bottom="1.5em" /></fo:table-cell>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Vorsitzende bzw. Vorsitzender des Promotionsausschusses</xsl:text></fo:block></fo:table-cell>
										</fo:table-row>
									</fo:table-body>
								</fo:table>
							</fo:block>
						</fo:block>
					</fo:block-container>
					<!-- Ermittlung der maximalen Seiten bzw. der Seitenzahl der letzten Seite-->
					<fo:block id="{generate-id(.)}"/>
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
	<xsl:template name="string-replace-all">
	  <xsl:param name="text" />
	  <xsl:param name="replace" />
	  <xsl:param name="by" />
	  <xsl:choose>
	    <xsl:when test="contains($text, $replace)">
	      <xsl:value-of select="substring-before($text,$replace)" />
	      <xsl:value-of select="$by" />
	      <xsl:call-template name="string-replace-all">
	        <xsl:with-param name="text" select="substring-after($text,$replace)" />
	        <xsl:with-param name="replace" select="$replace" />
	        <xsl:with-param name="by" select="$by" />
	      </xsl:call-template>
	    </xsl:when>
	    <xsl:otherwise>
	      <xsl:value-of select="$text" />
	    </xsl:otherwise>
	  </xsl:choose>
	</xsl:template>
</xsl:stylesheet>
