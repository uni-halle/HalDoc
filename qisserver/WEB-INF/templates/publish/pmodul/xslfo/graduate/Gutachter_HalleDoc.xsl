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
		<xsl:variable name="printLang"><xsl:value-of select="Person/Form/Language" /></xsl:variable>
		<xsl:variable name="evalGeschl">
			<xsl:for-each select="Person/MyPromo/Evaluator">
				<xsl:if test="ID = /publishDetail/Person/Form/EvalID"><xsl:value-of select="Geschl" /></xsl:if>
			</xsl:for-each>
		</xsl:variable>
		<xsl:variable name="evalTitel">
			<xsl:for-each select="Person/MyPromo/Evaluator">
				<xsl:if test="ID = /publishDetail/Person/Form/EvalID"><xsl:value-of select="Titel" /></xsl:if>
			</xsl:for-each>
		</xsl:variable>
		<xsl:variable name="evalNachname">
			<xsl:for-each select="Person/MyPromo/Evaluator">
				<xsl:if test="ID = /publishDetail/Person/Form/EvalID"><xsl:value-of select="nachname" /></xsl:if>
			</xsl:for-each>
		</xsl:variable>
		<xsl:variable name="evalVorname">
			<xsl:for-each select="Person/MyPromo/Evaluator">
				<xsl:if test="ID = /publishDetail/Person/Form/EvalID"><xsl:value-of select="vorname" /></xsl:if>
			</xsl:for-each>
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
					<fo:block-container position="absolute" left="25mm" top="55mm" width="83mm" height="33mm" border-top-style="solid" border-top-width="0.0pt" border-bottom-style="solid"
						border-bottom-width="0.0pt" border-left-style="solid" border-left-width="0.0pt" border-right-style="solid" border-right-width="0.0pt" font-family="{$font}">
						<fo:block text-align="start" font-size="10pt" line-height="14pt">
							<xsl:for-each select="Person/MyPromo/Evaluator">
								<xsl:if test="ID = /publishDetail/Person/Form/EvalID">
									<xsl:if test="Geschl='M'">
										<xsl:text>Herr </xsl:text>
									</xsl:if>
									<xsl:if test="Geschl='W'">
										<xsl:text>Frau </xsl:text>
									</xsl:if>
									<!--fo:block /-->
									<xsl:text> </xsl:text>
									<xsl:if test="Titel">
										<xsl:value-of select="Titel" />
										<xsl:text> </xsl:text>
									</xsl:if>
									<xsl:if test="AkadGradVor">
										<xsl:value-of select="AkadGradVor" />
										<xsl:text> </xsl:text>
									</xsl:if>
									<xsl:value-of select="vorname" />
									<xsl:text> </xsl:text>
									<xsl:value-of select="nachname" />
									<fo:block />
									<xsl:if test="Address/einrichtung">
										<xsl:value-of select="Address/einrichtung" />
										<fo:block />
									</xsl:if>
									<xsl:if test="Address/Institut">
										<xsl:value-of select="Address/Institut" />
										<fo:block />
									</xsl:if>
									<xsl:value-of select="Address/strasse" />
									<fo:block />
									<xsl:value-of select="Address/adrzusatz" />
									<fo:block />
									<xsl:value-of select="Address/plz" />
									<xsl:text> </xsl:text>
									<xsl:value-of select="Address/ort" />
								</xsl:if>
							</xsl:for-each>
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
							<xsl:if test="$printLang='en'">
								Promotion procedure for 
								<xsl:if test="Person/Geschl='M'">
									<xsl:text>Mr. </xsl:text><xsl:value-of select="Person/PerNac" />
								</xsl:if>
								<xsl:if test="Person/Geschl='W'">
									<xsl:text>Ms. </xsl:text><xsl:value-of select="Person/PerNac" />
								</xsl:if>
							</xsl:if>
							<xsl:if test="$printLang='de'">
								Promotionsverfahren von 
								<xsl:if test="Person/Geschl='M'">
									<xsl:text>Herrn </xsl:text><xsl:value-of select="Person/PerNac" />
								</xsl:if>
								<xsl:if test="Person/Geschl='W'">
									<xsl:text>Frau </xsl:text><xsl:value-of select="Person/PerNac" />
								</xsl:if>
							</xsl:if>
						</fo:block>
					</fo:block-container>
					<fo:block-container absolute-position="fixed" top="133.5mm" left="30mm" width="170mm">
						<fo:block color="#000000" text-align="start" font-family="{$font}">
							<xsl:if test="$printLang='en'">
								<xsl:if test="$evalGeschl='M'">
									<xsl:text>Dear Mr. </xsl:text>
								</xsl:if>
								<xsl:if test="$evalGeschl='W'">
									<xsl:text>Dear Ms. </xsl:text>
								</xsl:if>
							</xsl:if>
							<xsl:if test="$printLang='de'">
								<xsl:if test="$evalGeschl='M'">
									<xsl:text>Sehr geehrter Herr </xsl:text>
								</xsl:if>
								<xsl:if test="$evalGeschl='W'">
									<xsl:text>Sehr geehrte Frau </xsl:text>
								</xsl:if>
							</xsl:if>
							<xsl:value-of select="$evalTitel" />
							<xsl:text> </xsl:text>
							<xsl:value-of select="$evalNachname" />
						</fo:block>
					</fo:block-container>
					<fo:block-container absolute-position="fixed" top="143mm" left="30mm" width="165mm">
						<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="de">
							<xsl:if test="$printLang='en'">
								<xsl:variable name="doktorand">
									<xsl:if test="Person/Geschl='M'">
										<xsl:text>Mr. </xsl:text>
									</xsl:if>
									<xsl:if test="Person/Geschl='W'">
										<xsl:text>Ms. </xsl:text>
									</xsl:if>
									<xsl:value-of select="Person/PerNac" />
								</xsl:variable>
								the promotion commission of the <xsl:value-of select="Person/MyInfos/Einrichtung/ltxt" /> at Martin Luther University Halle-Wittenberg opens the promotion procedure for <xsl:value-of select="$doktorand" /> to obtain the doctorate in <xsl:value-of select="Person/MyPromo/AkadGrad" />. You were proposed and confirmed as expert to give your judgement about the dissertation.
							</xsl:if>
							<xsl:if test="$printLang='de'">
								<xsl:variable name="doktorand">
									<xsl:if test="Person/Geschl='M'">
										<xsl:text>Herrn </xsl:text>
									</xsl:if>
									<xsl:if test="Person/Geschl='W'">
										<xsl:text>Frau </xsl:text>
									</xsl:if>
									<xsl:value-of select="Person/PerNac" />
								</xsl:variable>
								der Promotionsausschuss der Einrichtung <xsl:value-of select="Person/MyInfos/Einrichtung/ltxt" /> der Martin-Luther-Universität Halle-Wittenberg eröffnete für <xsl:value-of select="$doktorand" /> ein Verfahren zur Erlangung des akademischen Grades <xsl:value-of select="Person/MyPromo/AkadGrad" />. Sie wurden als Gutachter/in der eingereichten Dissertation vorgeschlagen und bestätigt.
							</xsl:if>
						</fo:block>
						<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="de" padding-top="1em">
							<xsl:if test="$printLang='en'">
								Herewith I send copies of the dissertation, the abstract and our promotion order. I ask you to give your judgement according to paragraph 9 to the promotional order (which is unfortunately written in German).
							</xsl:if>
							<xsl:if test="$printLang='de'">
								Beiliegend übersende ich Ihnen ein Exemplar der Dissertation und bitte Sie, das Gutachten zu übernehmen und die Bewertung entsprechend § 9 der Promotionsordnung vorzunehmen.
							</xsl:if>
						</fo:block>
						<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="de" padding-top="1em">
							<xsl:if test="$printLang='en'">
								In the case of your approval to give the judgement, I would be very pleased of your send your judgement within <xsl:value-of select="Person/Form/Frist" /> weeks to me. After that you can keep the copy of the dissertation.
							</xsl:if>
							<xsl:if test="$printLang='de'">
								Im Falle Ihres Einverständnisses wäre ich Ihnen dankbar, wenn Sie mir Ihr Gutachten innerhalb von <xsl:value-of select="Person/Form/Frist" /> Wochen zusenden könnten. Die Dissertation geht dann in Ihren Besitz über.
							</xsl:if>
						</fo:block>
						<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="de" padding-top="1em">
							<xsl:if test="$printLang='en'">
								Many thanks in advance.
							</xsl:if>
							<xsl:if test="$printLang='de'">
								Für Ihre Mühe möchte ich mich bereits im Voraus bedanken.
							</xsl:if>
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" padding-top="2em">
							<xsl:if test="$printLang='en'">
								Yours sincerely,
							</xsl:if>
							<xsl:if test="$printLang='de'">
								Mit freundlichen Grüßen
							</xsl:if>
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
							<xsl:if test="$printLang='en'">
								Chair of the Doctoral Committee
							</xsl:if>
							<xsl:if test="$printLang='de'">
								<xsl:if test="Person/MyPromo/Vorsitz/Geschl='M'">
									<xsl:text>Vorsitzender des Promotionsausschusses</xsl:text>
								</xsl:if>
								<xsl:if test="Person/MyPromo/Vorsitz/Geschl='W'">
									<xsl:text>Vorsitzende des Promotionsausschusses</xsl:text>
								</xsl:if>
							</xsl:if>
						</fo:block>
					</fo:block-container>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>

</xsl:stylesheet>
<!-- AKTUELL:{$font} -->
<!-- vim: set encoding=utf-8 syntax=on tabstop=2 expandtab filetype=html background= : -->
