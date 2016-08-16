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
		<xsl:variable name="viereckColor"><xsl:value-of select="Person/MyInfos/Einrichtung/farbe" /></xsl:variable>
		<xsl:variable name="font">ZapfHumanist601BT-Roman</xsl:variable>
		<xsl:variable name="dokorand">
			<xsl:if test="Person/Geschl='M'">
				<xsl:text>Doktorand </xsl:text>
			</xsl:if>
			<xsl:if test="Person/Geschl='W'">
				<xsl:text>Doktorandin </xsl:text>
			</xsl:if>
		</xsl:variable>

		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="ersteSeite" page-height="297mm" page-width="210mm">
					<fo:region-body region-name="Body" margin-top="99mm" margin-left="15mm" margin-right="15mm" margin-bottom="15mm" background-color="white"
						background-repeat="no-repeat" />
					<fo:region-before region-name="KopfSeite1" extent="99mm" precedence="true" background-color="white" />
					<fo:region-after region-name="Fuss" extent="15mm" background-color="white" />
					<fo:region-start region-name="LRand" extent="10mm" background-color="white" />
					<fo:region-end region-name="RRand" extent="15mm" background-color="white" />
				</fo:simple-page-master>
				<fo:simple-page-master master-name="weitereSeiten" page-height="297mm" page-width="210mm">
					<fo:region-body region-name="Body" margin-top="19mm" margin-left="15mm" margin-right="10mm" margin-bottom="15mm" background-color="white"
						background-repeat="no-repeat" />
					<fo:region-before region-name="Kopf" extent="14mm" precedence="true" background-color="white" />
					<fo:region-after region-name="Fuss" extent="15mm" background-color="white" />
					<fo:region-start region-name="LRand" extent="10mm" background-color="white" />
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
							<xsl:value-of select="Person/MyInfos/Einrichtung/ltxt" />
						</fo:block>
						<fo:block padding-top="2mm" margin-left="2mm" space-before="0mm" font-family="{$font}" font-size="10pt" hyphenate="false" language="de" margin-right="15mm">
							<xsl:value-of select="Person/MyInfos/Einrichtung/utxt" />
						</fo:block>
					</fo:block-container>
					<!-- NEU KOPF NEU KOPF -->
					<fo:block />

					<!-- Adressfeld -->
					<fo:block-container position="absolute" left="23.1mm" top="49mm" width="90mm" height="45mm">
						<fo:block>
							<fo:instream-foreign-object>
							  <svg:svg xmlns:svg="http://www.w3.org/2000/svg" width="90mm" height="45mm">
							     <svg:circle cx="1mm" cy="1mm" r="0.25mm" stroke="rgb(0,0,0)"/>
							     <svg:circle cx="86mm" cy="1mm" r="0.25mm" stroke="rgb(0,0,0)"/>
							     <svg:circle cx="1mm" cy="39mm" r="0.25mm" stroke="rgb(0,0,0)"/>
							     <svg:circle cx="86mm" cy="39mm" r="0.25mm" stroke="rgb(0,0,0)"/>
							  </svg:svg>
							</fo:instream-foreign-object>
						</fo:block>
					</fo:block-container>
					<fo:block-container position="absolute" left="26mm" top="52mm" width="85mm" height="40mm">
						<fo:block text-align="start" font-family="{$font}" font-size="7pt">
							Martin-Luther-Universität Halle-Wittenberg, 06099 Halle (Saale)
						</fo:block>
					</fo:block-container>
					<fo:block-container position="absolute" left="24.5mm" top="60mm" width="85mm" height="38mm" border-top-style="solid" border-top-width="0.0pt" border-bottom-style="solid"
						border-bottom-width="0.0pt" border-left-style="solid" border-left-width="0.0pt" border-right-style="solid" border-right-width="0.0pt" font-family="{$font}">
						<fo:block text-align="start" font-size="10pt" line-height="14pt">
							<xsl:if test="Person/Geschl='M'">
								<xsl:text>Herr </xsl:text>
							</xsl:if>
							<xsl:if test="Person/Geschl='W'">
								<xsl:text>Frau </xsl:text>
							</xsl:if>
							<fo:block />
							<xsl:value-of select="Person/PerVor" />
							<xsl:text> </xsl:text>
							<xsl:value-of select="Person/PerNac" />
							<fo:block />
							<xsl:value-of select="Person/Dienstadresse/Strasse" />
							<fo:block />
							<xsl:value-of select="Person/Dienstadresse/PLZ" />
							<xsl:text> </xsl:text>
							<xsl:value-of select="Person/Dienstadresse/Ort" />
						</fo:block>
					</fo:block-container>
					<!-- NEU KOPF NEU KOPF -->
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
											<xsl:value-of select="Person/MyInfos/Einrichtung/ltxt" />
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
						<fo:block width="40mm" margin-left="3mm">
							<xsl:value-of select="Person/MyInfos/Dienstadresse/Strasse" />
						</fo:block>
						<fo:block width="40mm" margin-left="3mm">
							<xsl:value-of select="Person/MyInfos/Dienstadresse/PLZ" /><xsl:text> </xsl:text><xsl:value-of select="Person/MyInfos/Dienstadresse/Ort" />
						</fo:block>
					</fo:block-container>
					<fo:block-container position="absolute" left="60mm" height="15mm" width="40mm" border-left-style="solid" border-left-width="0.5pt" color="#000000" text-align="start" font-family="{$font}" font-size="8pt" line-height="10pt">
						<fo:block width="40mm" margin-left="3mm">
							<xsl:text>Tel </xsl:text><xsl:value-of select="Person/MyInfos/Dienstadresse/Telefon" />
						</fo:block>
						<fo:block width="40mm" margin-left="3mm">
							<xsl:text>Fax   </xsl:text><xsl:value-of select="Person/MyInfos/Dienstadresse/Fax" />
						</fo:block>
					</fo:block-container>
					<fo:block-container position="absolute" left="100mm" height="15mm" width="45mm" border-left-style="solid" border-left-width="0.5pt" 
						color="#000000" text-align="start" font-family="{$font}" font-size="8pt" line-height="10pt">
						<fo:block margin-left="3mm">
							e-mail:
						</fo:block>
						<fo:block margin-left="3mm" margin-right="3mm" text-align="start" hyphenate="true" language="de">
							<xsl:value-of select="Person/MyInfos/Dienstadresse/eMail" />
						</fo:block>
					</fo:block-container>
					<fo:block-container position="absolute" left="145mm" height="15mm" width="40mm" border-left-style="solid" border-left-width="0.5pt" color="#000000" text-align="start" font-family="{$font}" font-size="8pt" line-height="10pt">
						<fo:block margin-left="3mm">
							Internet:
						</fo:block>
						<fo:block margin-left="3mm" text-align="start" hyphenate="true" language="de">
							<xsl:value-of select="Person/MyInfos/Dienstadresse/Hyperlink" />
						</fo:block>
					</fo:block-container>
				</fo:static-content>

				<fo:flow flow-name="Body" font-family="{$font}" font-size="10pt">
					<fo:block-container absolute-position="fixed" top="119mm" left="30mm" width="170mm">
						<fo:block color="#000000" text-align="start" font-family="{$font}" font-weight="bold">
							Eröffnung Ihres Promotionsverfahrens
						</fo:block>
					</fo:block-container>
					<fo:block-container absolute-position="fixed" top="133.5mm" left="30mm" width="170mm">
						<fo:block color="#000000" text-align="start" font-family="{$font}">
							<xsl:if test="Person/Geschl='M'">
								<xsl:text>Sehr geehrter Herr </xsl:text>
							</xsl:if>
							<xsl:if test="Person/Geschl='W'">
								<xsl:text>Sehr geehrte Frau </xsl:text>
							</xsl:if>
							<xsl:value-of select="Person/PerNac" />
						</fo:block>
					</fo:block-container>
					<fo:block-container absolute-position="fixed" top="143mm" left="30mm" width="165mm">
						<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="de">
							nach Prüfung Ihres Antrages auf Zulassung zum Promotionsverfahren gemäß § 5 der Promotionsordnung der Einrichtung <xsl:value-of select="Person/MyInfos/Einrichtung/ltxt" /> wurde Ihr Promotionsverfahren am <xsl:value-of select="Person/MyPromo/Process/openingOfProcedure" /> eröffnet. Folgende Gutachterinnen bzw. Gutachter wurden benannt:
							<xsl:if test="Person/MyPromo/Mentor1/ID">
								<fo:list-block>
									<fo:list-item>
									  <fo:list-item-label start-indent="5pt">
									    <fo:block>&#x2022;</fo:block>
									  </fo:list-item-label>
									  <fo:list-item-body start-indent="15pt">
									    <fo:block>
												<xsl:if test="Person/MyPromo/Mentor1/Geschl='M'">
													<xsl:text>Herr </xsl:text>
												</xsl:if>
												<xsl:if test="Person/MyPromo/Mentor1/Geschl='W'">
													<xsl:text>Frau </xsl:text>
												</xsl:if>
												<xsl:if test="Person/MyPromo/Mentor1/Titel">
													<xsl:text> </xsl:text><xsl:value-of select="Person/MyPromo/Mentor1/Titel" /> 
												</xsl:if>
												<xsl:if test="Person/MyPromo/Mentor1/AkadGradVor">
													<xsl:text> </xsl:text><xsl:value-of select="Person/MyPromo/Mentor1/AkadGradVor" /> 
												</xsl:if>
												<xsl:if test="Person/MyPromo/Mentor1/namenbestand">
													<xsl:text> </xsl:text><xsl:value-of select="Person/MyPromo/Mentor1/namenbestand" /> 
												</xsl:if>
												<xsl:text> </xsl:text><xsl:value-of select="Person/MyPromo/Mentor1/nachname" />
									    </fo:block>
									  </fo:list-item-body>
									</fo:list-item>
									<xsl:if test="Person/MyPromo/Mentor2/ID">
										<fo:list-item>
										  <fo:list-item-label start-indent="5pt">
										    <fo:block>&#x2022;</fo:block>
										  </fo:list-item-label>
										  <fo:list-item-body start-indent="15pt">
										    <fo:block>
													<xsl:if test="Person/MyPromo/Mentor2/Geschl='M'">
														<xsl:text>Herr </xsl:text>
													</xsl:if>
													<xsl:if test="Person/MyPromo/Mentor2/Geschl='W'">
														<xsl:text>Frau </xsl:text>
													</xsl:if>
													<xsl:if test="Person/MyPromo/Mentor2/Titel">
														<xsl:text> </xsl:text><xsl:value-of select="Person/MyPromo/Mentor2/Titel" /> 
													</xsl:if>
													<xsl:if test="Person/MyPromo/Mentor2/AkadGradVor">
														<xsl:text> </xsl:text><xsl:value-of select="Person/MyPromo/Mentor2/AkadGradVor" /> 
													</xsl:if>
													<xsl:if test="Person/MyPromo/Mentor2/namenbestand">
														<xsl:text> </xsl:text><xsl:value-of select="Person/MyPromo/Mentor2/namenbestand" /> 
													</xsl:if>
													<xsl:text> </xsl:text><xsl:value-of select="Person/MyPromo/Mentor2/nachname" />
										    </fo:block>
										  </fo:list-item-body>
										</fo:list-item>
									</xsl:if>
									<xsl:if test="Person/MyPromo/Mentor3/ID">
										<fo:list-item>
										  <fo:list-item-label start-indent="5pt">
										    <fo:block>&#x2022;</fo:block>
										  </fo:list-item-label>
										  <fo:list-item-body start-indent="15pt">
										    <fo:block>
													<xsl:if test="Person/MyPromo/Mentor3/Geschl='M'">
														<xsl:text>Herr </xsl:text>
													</xsl:if>
													<xsl:if test="Person/MyPromo/Mentor3/Geschl='W'">
														<xsl:text>Frau </xsl:text>
													</xsl:if>
													<xsl:if test="Person/MyPromo/Mentor3/Titel">
														<xsl:text> </xsl:text><xsl:value-of select="Person/MyPromo/Mentor3/Titel" /> 
													</xsl:if>
													<xsl:if test="Person/MyPromo/Mentor3/AkadGradVor">
														<xsl:text> </xsl:text><xsl:value-of select="Person/MyPromo/Mentor3/AkadGradVor" /> 
													</xsl:if>
													<xsl:if test="Person/MyPromo/Mentor3/namenbestand">
														<xsl:text> </xsl:text><xsl:value-of select="Person/MyPromo/Mentor3/namenbestand" /> 
													</xsl:if>
													<xsl:text> </xsl:text><xsl:value-of select="Person/MyPromo/Mentor3/nachname" />
										    </fo:block>
										  </fo:list-item-body>
										</fo:list-item>
									</xsl:if>
								</fo:list-block>
							</xsl:if>
						</fo:block>
						<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="de" padding-top="1em">
							Als Wahlmitglied ...
						</fo:block>
						<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="de" padding-top="1em">
							<xsl:if test="Person/MyPromo/Vorsitz/Geschl='M'">
								<xsl:text>Vorsitzender der Promotionskommission ist Herr </xsl:text>
							</xsl:if>
							<xsl:if test="Person/MyPromo/Vorsitz/Geschl='W'">
								<xsl:text>Vorsitzende der Promotionskommission ist Frau </xsl:text>
							</xsl:if>
							<xsl:if test="Person/MyPromo/Vorsitz/Titel">
								<xsl:text> </xsl:text><xsl:value-of select="Person/MyPromo/Vorsitz/Titel" /> 
							</xsl:if>
							<xsl:if test="Person/MyPromo/Vorsitz/AkadGradVor">
								<xsl:text> </xsl:text><xsl:value-of select="Person/MyPromo/Vorsitz/AkadGradVor" /> 
							</xsl:if>
							<xsl:if test="Person/MyPromo/Vorsitz/namenbestand">
								<xsl:text> </xsl:text><xsl:value-of select="Person/MyPromo/Vorsitz/namenbestand" /> 
							</xsl:if>
							<xsl:text> </xsl:text><xsl:value-of select="Person/MyPromo/Vorsitz/nachname" /><xsl:text>.</xsl:text>
						</fo:block>
						<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="de" padding-top="1em">
							Nach Eingang der Gutachten wird über die Weiterführung des Verfahrens entschieden. Über diesen Beschluss werden Sie umgehend informiert.
						</fo:block>
						<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="de" padding-top="1em">
							Ich wünsche Ihnen einen erfolgreichen Verlauf Ihres Promotionsverfahrens.
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" padding-top="2em">
							Mit freundlichen Grüßen
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" padding-top="7em">
							<xsl:if test="Person/MyPromo/Vorsitz/Titel">
								<xsl:text> </xsl:text><xsl:value-of select="Person/MyPromo/Vorsitz/Titel" /> 
							</xsl:if>
							<xsl:if test="Person/MyPromo/Vorsitz/AkadGradVor">
								<xsl:text> </xsl:text><xsl:value-of select="Person/MyPromo/Vorsitz/AkadGradVor" /> 
							</xsl:if>
							<xsl:if test="Person/MyPromo/Vorsitz/namenbestand">
								<xsl:text> </xsl:text><xsl:value-of select="Person/MyPromo/Vorsitz/namenbestand" /> 
							</xsl:if>
							<xsl:text> </xsl:text><xsl:value-of select="Person/MyPromo/Vorsitz/nachname" />
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}">
							<xsl:if test="Person/MyPromo/Vorsitz/Geschl='M'">
								<xsl:text>Vorsitzender </xsl:text>
							</xsl:if>
							<xsl:if test="Person/MyPromo/Vorsitz/Geschl='W'">
								<xsl:text>Vorsitzende </xsl:text>
							</xsl:if>
							des Promotionsausschusses
						</fo:block>
					</fo:block-container>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>

</xsl:stylesheet>
<!-- AKTUELL:{$font} -->
<!-- vim: set encoding=utf-8 syntax=on tabstop=2 expandtab filetype=html background= : -->
