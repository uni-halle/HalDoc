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
		<xsl:variable name="fakId"><xsl:value-of select="/publishDetail/application/pluginReturn/valueElement/app_promoFaculty" /></xsl:variable>
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
		<xsl:variable name="fachName">
			<xsl:for-each select="/publishDetail/application/courseOfStudies/courseOfStudy">
				<xsl:if test="AbsID=/publishDetail/application/pluginReturn/valueElement/app_courseOfStudy">
					<xsl:value-of select="AbsDtx" />
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
				<!-- NEU NEU NEU -->
				<fo:static-content flow-name="KopfSeite1">
					<fo:block-container position="absolute" left="15mm" top="4mm">
						<fo:block />
					</fo:block-container>
				</fo:static-content>
				<!-- NEU NEU NUE -->

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
							<xsl:when test="contains($fakName,'Medizinische')">7</xsl:when>
							<xsl:otherwise>4</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<fo:block-container absolute-position="auto" break-before="auto">
						<xsl:choose>
							<xsl:when test="contains($fakName,'Wirtschaftswissenschaftliche')">
								<fo:block color="#000000" text-align="center" font-family="{$font}" font-size="12pt" font-weight="bold">
									Antrag auf Annahme als Doktorandin bzw. Doktorand gemäß den allgemeinen Bestimmungen für Promotionsordnungen an der Martin-Luther-Universität Halle-Wittenberg
								</fo:block>
								<fo:block color="#000000" text-align="center" font-family="{$font}" font-size="12pt" font-weight="bold" padding-bottom="4em">
									Application for acceptance as a doctoral candidate in accordance with the general provisions of the doctoral regulations at Martin Luther University Halle-Wittenberg
								</fo:block>
							</xsl:when>
							<xsl:otherwise>
								<fo:block color="#000000" text-align="center" font-family="{$font}" font-size="12pt" font-weight="bold">
									Antrag auf Annahme als Doktorandin bzw. Doktorand gemäß § <xsl:value-of select="$paragraph" /> der
								</fo:block>
								<fo:block color="#000000" text-align="center" font-family="{$font}" font-size="12pt" font-weight="bold">
									Promotionsordnung <xsl:value-of select="$artikel" /><xsl:text> </xsl:text><xsl:value-of select="$fakName" /> an der
								</fo:block>
								<fo:block color="#000000" text-align="center" font-family="{$font}" font-size="12pt" font-weight="bold">
									Martin-Luther-Universität Halle-Wittenberg
								</fo:block>
								<fo:block color="#000000" text-align="center" font-family="{$font}" font-size="12pt" font-weight="bold" padding-bottom="4em">
									Application for acceptance as a doctoral candidate in accordance with § <xsl:value-of select="$paragraph" /> of the doctoral regulations of the <xsl:value-of select="$fakNameEn" /> at the Martin Luther University Halle-Wittenberg
								</fo:block>
							</xsl:otherwise>
						</xsl:choose>
						<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="de" padding-bottom="1em">
							<fo:inline font-weight="bold">im Institut / at the Institute:</fo:inline><xsl:text>&#xA0;&#xA0;&#xA0;&#xA0;</xsl:text><xsl:value-of select="application/pluginReturn/valueElement/app_institute" />
						</fo:block>
						<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="de">
							In Kenntnis der Bestimmungen über die Voraussetzungen und Verfahrensfragen zur Promotion beantrage ich hiermit die Annahme als Doktorandin bzw. Doktorand.
						</fo:block>
						<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="en" padding-bottom="2em">
							I hereby acknowledge the formal regulations regarding the conditions and matters of the doctorate procedure and apply for the acceptance as a doctoral candidate.
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block font-weight="bold">Persönliche Daten / Personal data:</fo:block>
							<fo:block>
								<fo:table>
									<fo:table-column column-width="8cm"/>
									<fo:table-column column-width="0.5cm"/>
									<fo:table-column column-width="8cm"/>
									<fo:table-body>
										<fo:table-row>
											<fo:table-cell>
												<fo:block>
													<xsl:value-of select="application/pluginReturn/valueElement/app_lastname" />
													<!-- app_birthname -->
													<xsl:if test="application/pluginReturn/valueElement/app_birthname">, geb. <xsl:value-of select="application/pluginReturn/valueElement/app_birthname" /></xsl:if>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell><fo:block /></fo:table-cell>
											<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/valueElement/app_firstname" /></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Name und Geburtsname / Surname and name at birth</xsl:text></fo:block></fo:table-cell>
											<fo:table-cell><fo:block  padding-bottom="1.5em" /></fo:table-cell>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Vorname / First name</xsl:text></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/valueElement/app_dateOfBirth" /></fo:block></fo:table-cell>
											<fo:table-cell><fo:block /></fo:table-cell>
											<fo:table-cell>
												<fo:block>
													<xsl:value-of select="application/pluginReturn/valueElement/app_placeOfBirth" />
													<xsl:text>, </xsl:text>
													<xsl:for-each select="application/Countries/Country">
														<xsl:if test="akfz=/publishDetail/application/pluginReturn/valueElement/app_countryOfBirth">
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
														<xsl:if test="akfz=/publishDetail/application/pluginReturn/valueElement/app_national">
															<xsl:value-of select="dtxt" />
														</xsl:if>
													</xsl:for-each>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell><fo:block /></fo:table-cell>
											<fo:table-cell>
												<fo:block>
													<xsl:choose>
														<xsl:when test="application/pluginReturn/valueElement/app_gender='M'">männlich</xsl:when>
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
							<xsl:if test="contains($fakName,'Medizinische')">
								<fo:block>
									<fo:table>
										<fo:table-column column-width="8cm"/>
										<fo:table-column column-width="0.5cm"/>
										<fo:table-column column-width="8cm"/>
										<fo:table-body>
											<fo:table-row>
												<fo:table-cell>
													<fo:block>
														<xsl:for-each select="application/SemesterList/Semester">
															<xsl:if test="id=/publishDetail/application/pluginReturn/valueElement/app_studyStartSemester">
																<xsl:value-of select="dtxt" />
															</xsl:if>
														</xsl:for-each>
													</fo:block>
												</fo:table-cell>
												<fo:table-cell><fo:block /></fo:table-cell>
												<fo:table-cell>
													<fo:block>
														<xsl:for-each select="application/SemesterList/Semester">
															<xsl:if test="id=/publishDetail/application/pluginReturn/valueElement/app_studyEndSemester">
																<xsl:value-of select="dtxt" />
															</xsl:if>
														</xsl:for-each>
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Studienzeitraum Beginn / Duration of study start</xsl:text></fo:block></fo:table-cell>
												<fo:table-cell><fo:block  padding-bottom="1.5em" /></fo:table-cell>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Studienzeitraum Ende / Duration of study end</xsl:text></fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/valueElement/app_physDate" /></fo:block></fo:table-cell>
												<fo:table-cell><fo:block /></fo:table-cell>
												<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/valueElement/app_pjDate" /></fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>ggf. Datum des  Physikums / date of first state examination, if applicable</xsl:text></fo:block></fo:table-cell>
												<fo:table-cell><fo:block  padding-bottom="1.5em" /></fo:table-cell>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Datum Beginn PJ / Date start of PJ</xsl:text></fo:block></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell>
													<fo:block>
														<xsl:for-each select="application/SemesterList/Semester">
															<xsl:if test="id=/publishDetail/application/aktsem">
																<xsl:value-of select="dtxt" />
															</xsl:if>
														</xsl:for-each>
													</fo:block>
												</fo:table-cell>
												<fo:table-cell><fo:block /></fo:table-cell>
												<fo:table-cell><fo:block /></fo:table-cell>
											</fo:table-row>
											<fo:table-row>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Semester zum Antragszeitpunkt Annahme Doktorandin bzw. Doktorand / Semester of graduation acceptance as a doctoral candidate</xsl:text></fo:block></fo:table-cell>
												<fo:table-cell><fo:block  padding-bottom="1.5em" /></fo:table-cell>
												<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block /></fo:table-cell>
											</fo:table-row>
										</fo:table-body>
									</fo:table>
								</fo:block>
							</xsl:if>
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block font-weight="bold">Wohnanschrift / Present address:</fo:block>
							<fo:block>
								<fo:table>
									<fo:table-column column-width="8cm"/>
									<fo:table-column column-width="0.5cm"/>
									<fo:table-column column-width="8cm"/>
									<fo:table-body>
										<fo:table-row>
											<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/valueElement/app_street" /></fo:block></fo:table-cell>
											<fo:table-cell><fo:block /></fo:table-cell>
											<fo:table-cell>
												<fo:block>
													<xsl:value-of select="application/pluginReturn/valueElement/app_postcode" /><xsl:text> - </xsl:text><xsl:value-of select="application/pluginReturn/valueElement/app_city" />
													<xsl:text>, </xsl:text>
													<xsl:for-each select="application/Countries/Country">
														<xsl:if test="akfz=/publishDetail/application/pluginReturn/valueElement/app_country">
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
											<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/valueElement/app_telefon" /></fo:block></fo:table-cell>
											<fo:table-cell><fo:block /></fo:table-cell>
											<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/valueElement/app_email" /></fo:block></fo:table-cell>
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
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="de" padding-bottom="2em">
							<fo:table>
								<fo:table-column column-width="1.5cm"/>
								<fo:table-column column-width="14.5cm"/>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2"><fo:block font-weight="bold"><xsl:text>Aktueller Status der Antragstellerin bzw. des Antragsstellers</xsl:text></fo:block></fo:table-cell>
									</fo:table-row>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Wissenschaftliche Mitarbeiterin bzw. Wissenschaftlicher Mitarbeiter der Universität Halle (Haushalt) / Scientific staff at the University of Halle (Budget)</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="application/pluginReturn/valueElement/app_appStatus = '1'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">0</xsl:with-param>
										<xsl:with-param name="additional">0</xsl:with-param>
									</xsl:call-template>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Wissenschaftliche Mitarbeiterin bzw. Wissenschaftlicher Mitarbeiter der Universität Halle (Drittmittel) / Scientific staff at the University of Halle (External funds)</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="application/pluginReturn/valueElement/app_appStatus = '2'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">0</xsl:with-param>
										<xsl:with-param name="additional">0</xsl:with-param>
									</xsl:call-template>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Stipendiatin bzw. Stipendiat / Scholarship holder</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="application/pluginReturn/valueElement/app_appStatus = '3'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">0</xsl:with-param>
										<xsl:with-param name="additional">0</xsl:with-param>
									</xsl:call-template>
									<xsl:variable name="businessAddress1">
										<xsl:if test="application/pluginReturn/valueElement/app_appStatus = '4'">
											<xsl:value-of select="application/pluginReturn/valueElement/app_buisnessInstitute" /><xsl:text>, </xsl:text><xsl:value-of select="application/pluginReturn/valueElement/app_buisnessStreet" /><xsl:text>, </xsl:text><xsl:value-of select="application/pluginReturn/valueElement/AddressBusiness/plz" /> <xsl:value-of select="application/pluginReturn/valueElement/app_buisnessPostcode" /><xsl:text>, </xsl:text><xsl:value-of select="application/pluginReturn/valueElement/app_buisnessTelefon" />
										</xsl:if>
									</xsl:variable>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Externe Doktorandin bzw. externer Doktorand/External doctoral candidate</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="application/pluginReturn/valueElement/app_appStatus = '4'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">gegebenenfalls Institution, Adresse und Tel.-Nr. / Institution, address and telephone number, if applicable:</xsl:with-param>
										<xsl:with-param name="additional"><xsl:choose><xsl:when test="normalize-space($businessAddress1) = ''">0</xsl:when><xsl:otherwise><xsl:value-of select="$businessAddress1" /></xsl:otherwise></xsl:choose></xsl:with-param>
									</xsl:call-template>
									<xsl:variable name="businessAddress2">
										<xsl:if test="application/pluginReturn/valueElement/app_appStatus = '5'">
											<xsl:value-of select="application/pluginReturn/valueElement/app_buisnessInstitute" /><xsl:text>, </xsl:text><xsl:value-of select="application/pluginReturn/valueElement/app_buisnessStreet" /><xsl:text>, </xsl:text><xsl:value-of select="application/pluginReturn/valueElement/AddressBusiness/plz" /> <xsl:value-of select="application/pluginReturn/valueElement/app_buisnessPostcode" /><xsl:text>, </xsl:text><xsl:value-of select="application/pluginReturn/valueElement/app_buisnessTelefon" />
										</xsl:if>
									</xsl:variable>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Anderes / Others</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="application/pluginReturn/valueElement/app_appStatus = '5'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">gegebenenfalls Institution, Adresse und Tel.-Nr. / Institution, address and telephone number, if applicable:</xsl:with-param>
										<xsl:with-param name="additional"><xsl:choose><xsl:when test="normalize-space($businessAddress2) = ''">0</xsl:when><xsl:otherwise><xsl:value-of select="$businessAddress2" /></xsl:otherwise></xsl:choose></xsl:with-param>
									</xsl:call-template>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block-container>
					<xsl:if test="contains($fakName,'Medizinische')">
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
											<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/valueElement/app_ethical) = 'j'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
											<xsl:with-param name="additionalPre">Datum der Beurteilung / Date of the evaluation</xsl:with-param>
											<xsl:with-param name="additional"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/valueElement/app_ethicalAdd) = ''">0</xsl:when><xsl:when test="normalize-space(application/pluginReturn/valueElement/app_ethical) != 'j'">0</xsl:when><xsl:otherwise><xsl:value-of select="application/pluginReturn/valueElement/app_ethicalAdd" /></xsl:otherwise></xsl:choose></xsl:with-param>
										</xsl:call-template>
										<xsl:call-template name="checkField">
											<xsl:with-param name="text">Nein / No</xsl:with-param>
											<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/valueElement/app_ethical) = 'j'">0</xsl:when><xsl:otherwise>1</xsl:otherwise></xsl:choose></xsl:with-param>
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
											<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/valueElement/app_resTrGroup) = 'j'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">Name des Kollegs / Name of the group:</xsl:with-param>
										<xsl:with-param name="additional"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/valueElement/app_resTrGroupAdd) = ''">0</xsl:when><xsl:when test="normalize-space(application/pluginReturn/valueElement/app_resTrGroup) != 'j'">0</xsl:when><xsl:otherwise><xsl:value-of select="application/pluginReturn/valueElement/app_resTrGroupAdd" /></xsl:otherwise></xsl:choose></xsl:with-param>
										</xsl:call-template>
										<xsl:call-template name="checkField">
											<xsl:with-param name="text">Nein / No</xsl:with-param>
											<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/valueElement/app_resTrGroup) = 'j'">0</xsl:when><xsl:otherwise>1</xsl:otherwise></xsl:choose></xsl:with-param>
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
											<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/valueElement/app_statistic) = 'j'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">Name der Einrichtung / Name of the institution:</xsl:with-param>
										<xsl:with-param name="additional"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/valueElement/app_statisticAdd) = ''">0</xsl:when><xsl:when test="normalize-space(application/pluginReturn/valueElement/app_statistic) != 'j'">0</xsl:when><xsl:otherwise><xsl:value-of select="application/pluginReturn/valueElement/app_statisticAdd" /></xsl:otherwise></xsl:choose></xsl:with-param>
										</xsl:call-template>
										<xsl:call-template name="checkField">
											<xsl:with-param name="text">Nein / No</xsl:with-param>
											<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/valueElement/app_statistic) = 'j'">0</xsl:when><xsl:otherwise>1</xsl:otherwise></xsl:choose></xsl:with-param>
											<xsl:with-param name="additionalPre">0</xsl:with-param>
											<xsl:with-param name="additional">0</xsl:with-param>
										</xsl:call-template>
									</fo:table-body>
								</fo:table>
							</fo:block>
						</fo:block-container>
					</xsl:if>
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="de" padding-bottom="2em">
							<fo:table>
								<fo:table-column column-width="1.5cm"/>
								<fo:table-column column-width="14.5cm"/>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2"><fo:block>Die Erarbeitung der Dissertation erfolgt im Rahmen eines strukturierten Promotionsprogrammes. / The doctoral dissertation will be made within the framework of a structured doctoral program.</fo:block></fo:table-cell>
									</fo:table-row>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Ja / Yes</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/valueElement/app_prog) = 'j'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">gegebenenfalls Institution und Bezeichnung des Programmes / Institution and name of the program, if applicable:</xsl:with-param>
										<xsl:with-param name="additional"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/valueElement/app_progAdd) = ''">0</xsl:when><xsl:when test="normalize-space(application/pluginReturn/valueElement/app_prog) != 'j'">0</xsl:when><xsl:otherwise><xsl:value-of select="application/pluginReturn/valueElement/app_progAdd" /></xsl:otherwise></xsl:choose></xsl:with-param>
									</xsl:call-template>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Nein / No</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/valueElement/app_prog) = 'j'">0</xsl:when><xsl:otherwise>1</xsl:otherwise></xsl:choose></xsl:with-param>
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
										<fo:table-cell number-columns-spanned="2"><fo:block>Die Erarbeitung der Dissertation erfolgt im Rahmen eines bi-nationalen Promotionsverfahrens (Cotutelle de thèse). / The doctoral dissertation will be made within the framework of a double doctorate (Cotutelle de thèse).</fo:block></fo:table-cell>
									</fo:table-row>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Ja / Yes</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/valueElement/app_binat) = 'j'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">gegebenenfalls Institution und Land / Institution und country, if applicable:</xsl:with-param>
										<xsl:with-param name="additional"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/valueElement/app_binatAdd) = ''">0</xsl:when><xsl:when test="normalize-space(application/pluginReturn/valueElement/app_binat) != 'j'">0</xsl:when><xsl:otherwise><xsl:value-of select="application/pluginReturn/valueElement/app_binatAdd" /></xsl:otherwise></xsl:choose></xsl:with-param>
									</xsl:call-template>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Nein / No</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/valueElement/app_binat) = 'j'">0</xsl:when><xsl:otherwise>1</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">0</xsl:with-param>
										<xsl:with-param name="additional">0</xsl:with-param>
									</xsl:call-template>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block-container>
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block font-weight="bold">Bereits erworbene akademische Grade / Academic degrees achieved so far:</fo:block>
							<fo:table>
								<fo:table-column column-width="4cm"/>
								<fo:table-column column-width="3.5cm"/>
								<fo:table-column column-width="4cm"/>
								<fo:table-column column-width="4.5cm"/>
								<fo:table-header>
									<fo:table-row>
										<fo:table-cell><fo:block>Akad. Grad / Academic degree</fo:block></fo:table-cell>
										<fo:table-cell><fo:block>Wann / When</fo:block></fo:table-cell>
										<fo:table-cell><fo:block>Wo / Where</fo:block></fo:table-cell>
										<fo:table-cell><fo:block>An einer Fachhochschule? / At a University of Applied Sciences?</fo:block></fo:table-cell>
									</fo:table-row>
								</fo:table-header>
								<fo:table-body>
									<xsl:for-each select="application/pluginReturn/valueElement/degrees/*">
										<xsl:variable name="degree"><xsl:value-of select="degree" /></xsl:variable>
										<xsl:variable name="degreeText">
											<xsl:for-each select="/publishDetail/application/AkadGrades/AkadGrade">
												<xsl:if test="gradid=$degree">
													<xsl:value-of select="ltxt" />
												</xsl:if>
											</xsl:for-each>
										</xsl:variable>
										<fo:table-row>
											<fo:table-cell>
												<fo:block>
													<xsl:choose>
														<xsl:when test="normalize-space($degreeText) = '' and normalize-space(degree_add) != ''"><xsl:value-of select="degree_add" /></xsl:when>
														<xsl:otherwise><xsl:value-of select="$degreeText" /></xsl:otherwise>
													</xsl:choose>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell><fo:block><xsl:value-of select="when" /></fo:block></fo:table-cell>
											<fo:table-cell><fo:block><xsl:value-of select="where" /></fo:block></fo:table-cell>
											<fo:table-cell><fo:block>
												<xsl:choose>
													<xsl:when test="fh = 'on'">Ja</xsl:when>
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
							<fo:block font-weight="bold">Fachgebiet der angestrebten Promotion / Field of intended doctorate (gemäß Fächerkatalog der Promotionsordnung der Einrichtung):</fo:block>
							<fo:block><xsl:value-of select="$fachName" /></fo:block>
						</fo:block>
					</fo:block-container>
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block font-weight="bold">Angestrebter akademischer Grad / Intended academic degree:</fo:block>
							<fo:table>
								<fo:table-column column-width="1.5cm"/>
								<fo:table-column column-width="14.5cm"/>
								<fo:table-body>
									<xsl:for-each select="application/AkadGradesForPromotion/AkadGrade">
										<xsl:call-template name="checkField">
											<xsl:with-param name="text"><xsl:value-of select="ltxt" /> (<xsl:value-of select="dtxt" />)</xsl:with-param>
											<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="gradid = /publishDetail/application/pluginReturn/valueElement/app_gettingGrade">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
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
							<fo:block font-weight="bold"><xsl:text>Arbeitstitel der Dissertation / Working title of the Dissertation: </xsl:text></fo:block>
							<fo:block padding-bottom="1em"><xsl:value-of select="application/pluginReturn/valueElement/app_title" /></fo:block>
							<fo:block><fo:inline font-weight="bold"><xsl:text>Beginn der Arbeit an der Dissertation / Start of the doctoral studies: </xsl:text></fo:inline><xsl:value-of select="application/pluginReturn/valueElement/app_startingDate" /></fo:block>
						</fo:block>
					</fo:block-container>
					<xsl:if test="contains($fakName,'Medizinische')">
						<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
							<fo:block padding-top="1em">
								<fo:inline font-weight="bold"><xsl:text>Sprache der einzureichenden Dissertation / Language of the dissertation: </xsl:text></fo:inline>
								<xsl:for-each select="application/Languages/Language">
									<xsl:if test="id=/publishDetail/application/pluginReturn/valueElement/app_dissLang">
										<xsl:value-of select="dtxt" />
									</xsl:if>
								</xsl:for-each>
							</fo:block>
							<fo:block padding-bottom="1em">
								<fo:inline font-weight="bold"><xsl:text>Sprache der Verteidigung / Language of condruct the defense: </xsl:text></fo:inline>
								<xsl:for-each select="application/Languages/Language">
									<xsl:if test="id=/publishDetail/application/pluginReturn/valueElement/app_vertLang">
										<xsl:value-of select="dtxt" />
									</xsl:if>
								</xsl:for-each>
							</fo:block>
						</fo:block-container>
						<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
							<fo:block padding-bottom="1em">
								<fo:inline font-weight="bold"><xsl:text>Forschungsschwerpunkt / Center of research: </xsl:text></fo:inline>
								<xsl:for-each select="application/ResearchCenters/ResearchCenter">
									<xsl:if test="id=/publishDetail/application/pluginReturn/valueElement/app_centResearch or (normalize-space(/publishDetail/application/pluginReturn/valueElement/app_centResearch) = '' and id=0)">
										<xsl:value-of select="dtxt" />
									</xsl:if>
								</xsl:for-each>
							</fo:block>
						</fo:block-container>
						<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
							<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="de" padding-bottom="2em">
								<fo:table>
									<fo:table-column column-width="1.5cm"/>
									<fo:table-column column-width="14.5cm"/>
									<fo:table-body>
										<fo:table-row>
											<fo:table-cell number-columns-spanned="2"><fo:block>Frühere Promotionsversuche / previous promotion attempts</fo:block></fo:table-cell>
										</fo:table-row>
										<xsl:call-template name="checkField">
											<xsl:with-param name="text">Ja / Yes</xsl:with-param>
											<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/valueElement/app_prevPromo) = 'j'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
										<xsl:with-param name="additionalPre">Name des Kollegs / Name of the group:</xsl:with-param>
										<xsl:with-param name="additional"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/valueElement/app_prevPromoAdd) = ''">0</xsl:when><xsl:when test="normalize-space(application/pluginReturn/valueElement/app_prevPromo) != 'j'">0</xsl:when><xsl:otherwise><xsl:value-of select="application/pluginReturn/valueElement/app_prevPromoAdd" /></xsl:otherwise></xsl:choose></xsl:with-param>
										</xsl:call-template>
										<xsl:call-template name="checkField">
											<xsl:with-param name="text">Nein / No</xsl:with-param>
											<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="normalize-space(application/pluginReturn/valueElement/app_prevPromo) = 'j'">0</xsl:when><xsl:otherwise>1</xsl:otherwise></xsl:choose></xsl:with-param>
											<xsl:with-param name="additionalPre">0</xsl:with-param>
											<xsl:with-param name="additional">0</xsl:with-param>
										</xsl:call-template>
									</fo:table-body>
								</fo:table>
							</fo:block>
						</fo:block-container>
					</xsl:if>
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:table>
								<fo:table-column column-width="16.5cm"/>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell><fo:block font-weight="bold">Betreuende Hochschullehrerin bzw. betreuender Hochschullehrer / Mentoring professor (1):</fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/valueElement/app_tutor1Name" /></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt" padding-bottom="1em">Akad. Titel, akad. Grad, Name, Vorname / Academic title, degree, surname, first name:</fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/valueElement/app_tutor1Institute" /></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt">Wissenschaftl. Einrichtung, Institut / Sientific institution, institute:</fo:block></fo:table-cell>
									</fo:table-row>
									<xsl:if test="normalize-space(application/pluginReturn/valueElement/app_tutor2Name) != ''">
										<fo:table-row>
											<fo:table-cell padding-top="1em">
												<fo:block>Gegebenenfalls / If applicable:</fo:block>
												<fo:block font-weight="bold">Betreuende Hochschullehrerin bzw. betreuender Hochschullehrer / Mentoring professor (2):</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/valueElement/app_tutor2Name" /></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt" padding-bottom="1em">Akad. Titel, akad. Grad, Name, Vorname / Academic title, degree, surname, first name:</fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/valueElement/app_tutor2Institute" /></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt">Wissenschaftl. Einrichtung, Institut / Sientific institution, institute:</fo:block></fo:table-cell>
										</fo:table-row>
									</xsl:if>
									<xsl:if test="normalize-space(application/pluginReturn/valueElement/app_tutor3Name) != ''">
										<fo:table-row>
											<fo:table-cell padding-top="1em">
												<fo:block>Gegebenenfalls / If applicable:</fo:block>
												<fo:block font-weight="bold">Betreuende Hochschullehrerin bzw. betreuender Hochschullehrer / Mentoring professor (3):</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/valueElement/app_tutor3Name" /></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt" padding-bottom="1em">Akad. Titel, akad. Grad, Name, Vorname / Academic title, degree, surname, first name:</fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell><fo:block><xsl:value-of select="application/pluginReturn/valueElement/app_tutor3Institute" /></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt">Wissenschaftl. Einrichtung, Institut / Sientific institution, institute:</fo:block></fo:table-cell>
										</fo:table-row>
									</xsl:if>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block-container>
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always" padding-bottom="1em">
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de">Kenntnisnahme und Bestätigung der Bereitschaft der betreuenden Hochschullehrerin bzw. des betreuenden Hochschullehrers / Confirmation of willingness of the mentoring professor (1)</fo:block>
						<fo:block font-size="8pt" padding-top="6em" padding-bottom="1em">
							<fo:table>
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
					<xsl:if test="normalize-space(application/pluginReturn/valueElement/app_tutor2Name) != ''">
						<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
							<fo:block hyphenate="true" language="de">Kenntnisnahme und Bestätigung der Bereitschaft der betreuenden Hochschullehrerin bzw. des betreuenden Hochschullehrers / Confirmation of willingness of the mentoring professor (2) </fo:block>
							<fo:block font-size="8pt" padding-top="6em" padding-bottom="1em">
								<fo:table>
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
					<xsl:if test="normalize-space(application/pluginReturn/valueElement/app_tutor3Name) != ''">
						<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
							<fo:block hyphenate="true" language="de">Kenntnisnahme und Bestätigung der Bereitschaft der betreuenden Hochschullehrerin bzw. des betreuenden Hochschullehrers / Confirmation of willingness of the mentoring professor (3) </fo:block>
							<fo:block font-size="8pt" padding-top="6em" padding-bottom="1em">
								<fo:table>
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
					<xsl:if test="not(contains($fakName,'Theologische'))">
						<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
							<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
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
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block font-weight="bold">Erklärung / Declaration</fo:block>
							<fo:block>Ich erkläre, dass ich mich an keiner anderen Hochschule einem Promotionsverfahren unterzogen bzw. eine Promotion begonnen habe. / I declare that I have not completed or initiated a doctorate procedure at any other university.</fo:block>
							<xsl:if test="contains($fakName,'Naturwissenschaftliche') or contains($fakName,'Zentrum')">
								<fo:block padding-top="1em">Hiermit erkläre ich, dass ich weder vorbestraft bin, noch dass gegen mich Ermittlungsverfahren anhängig sind. / I hereby declare that I have no criminal record and that no preliminary investigations are pending against me.</fo:block>
							</xsl:if>
							<fo:block font-size="8pt" padding-top="6em">
								<fo:table>
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
					<xsl:if test="contains($fakName,'Wirtschaftswiss. Bereich')">
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
							<fo:block>Anlagen / Enclosures:</fo:block>
							<fo:block>
								<fo:list-block>
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
									<xsl:if test="contains($fakName,'Theologische')">
										<fo:list-item>
											<fo:list-item-label end-indent="label-end()"><fo:block>-</fo:block></fo:list-item-label>
											<fo:list-item-body start-indent="body-start()"><fo:block>Nachweis über Hebraicum/Graecum/Latinum (ggf. äquivalente Abschlüsse) / certificates of Hebraicum/Graecum/Latinum (equivalent degrees, if applicable)</fo:block></fo:list-item-body>
										</fo:list-item>
										<fo:list-item>
											<fo:list-item-label end-indent="label-end()"><fo:block>-</fo:block></fo:list-item-label>
											<fo:list-item-body start-indent="body-start()"><fo:block>Nachweis über das aktive Beherrschen der deutschen oder der englischen Sprache als Wissenschaftssprache / proof of language abilities in German or English on an academic level</fo:block></fo:list-item-body>
										</fo:list-item>
										<fo:list-item>
											<fo:list-item-label end-indent="label-end()"><fo:block>-</fo:block></fo:list-item-label>
											<fo:list-item-body start-indent="body-start()"><fo:block>Nachweis über die Kirchenzugehörigkeit / certificate of membership in a church</fo:block></fo:list-item-body>
										</fo:list-item>
									</xsl:if>
								</fo:list-block>
							</fo:block>
						</fo:block>
					</fo:block-container>
					<fo:block break-after="page" />
					<fo:block-container absolute-position="auto" break-before="auto" keep-together.within-column="always">
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							<fo:block font-weight="bold">Bearbeitungsbogen <xsl:value-of select="$artikel" /><xsl:text> </xsl:text><xsl:value-of select="$fakName" /> / Editing form of the <xsl:value-of select="$fakNameEn" /></fo:block>
							<fo:block>(Wird von <xsl:value-of select="$artikel" /><xsl:text> </xsl:text><xsl:value-of select="$fakName" /> ausgefüllt / To be filled in by the <xsl:value-of select="$fakNameEn" />)</fo:block>
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							<fo:block>Die gemäß der Promotionsordnung <xsl:value-of select="$artikel" /><xsl:text> </xsl:text><xsl:value-of select="$fakName" /> der Martin-Luther-Universität Halle-Wittenberg einzureichenden Unterlagen wurden vollzählig und ordnungsgemäß vorgelegt.</fo:block>
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							<fo:block font-size="8pt" padding-top="6em">
								<fo:table>
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
						<xsl:variable name="paragraph2">
							<xsl:choose>
								<xsl:when test="contains($fakName,'Wirtschaftswiss. Bereich')">7</xsl:when>
								<xsl:otherwise>5</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<xsl:variable name="paragraph3">
							<xsl:choose>
								<xsl:when test="contains($fakName,'Wirtschaftswiss. Bereich')">5</xsl:when>
								<xsl:otherwise>3</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							<fo:block font-weight="bold">Prüfung und Zustimmung durch den Promotionsausschuss </fo:block>
							<fo:table>
								<fo:table-column column-width="1.5cm"/>
								<fo:table-column column-width="14.5cm"/>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell><fo:block text-align="center"><fo:inline font-family="ZapfDingbats">&#x274F;</fo:inline></fo:block></fo:table-cell>
										<fo:table-cell><fo:block>Dem Antrag auf Annahme als Doktorandin bzw. Doktorand für das Fachgebiet <xsl:value-of select="$fachName" /> wird entsprochen.</fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell padding-top="1em"><fo:block text-align="center"><fo:inline font-family="ZapfDingbats">&#x274F;</fo:inline></fo:block></fo:table-cell>
										<fo:table-cell padding-top="1em">
											<fo:block>
												Die Zulassung zum Promotionsverfahren gemäß § <xsl:value-of select="$paragraph2" /> der Promotionsordnung der Einrichtung <xsl:value-of select="$fakName" /> an der Martin-Luther-Universität Halle-Wittenberg wird nur 
												gewährt, wenn zur Erfüllung der Zulassungsvoraussetzungen zur Promotion gemäß § <xsl:value-of select="$paragraph3" /> der Promotionsordnung zum Zeitpunkt des Antrages auf Zulassung zum Promotionsverfahren der 
												Nachweis über folgende Leistungen erbracht wird:
											</fo:block>
										</fo:table-cell>
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
										<fo:table-cell padding-top="1em"><fo:block>Dem Antrag auf Annahme als Doktorandin bzw. Doktorand für das Fachgebiet <xsl:value-of select="$fachName" /> wird nicht entsprochen. Der Mitteilung über die Ablehnung des Antrages liegt eine gesonderte Begründung und Rechtsbehelfsbelehrung bei.</fo:block></fo:table-cell>
									</fo:table-row>
								</fo:table-body>
							</fo:table>
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							<fo:block font-size="8pt" padding-top="6em">
								<fo:table>
									<fo:table-column column-width="7cm"/>
									<fo:table-column column-width="0.5cm"/>
									<fo:table-column column-width="9cm"/>
									<fo:table-body>
										<fo:table-row>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Ort, Datum</xsl:text></fo:block></fo:table-cell>
											<fo:table-cell><fo:block  padding-bottom="1.5em" /></fo:table-cell>
											<fo:table-cell border-top-style="solid" border-top-width="0.25pt"><fo:block font-size="8pt"><xsl:text>Unterschrift der Vorsitzenden bzw. des Vorsitzenden des Promotionsausschusses</xsl:text></fo:block></fo:table-cell>
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
