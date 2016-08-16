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
					<fo:region-after region-name="FussSeite1" extent="15mm" background-color="white" />
					<fo:region-start region-name="LRandSeite1" extent="10mm" background-color="white" />
					<fo:region-end region-name="RRandSeite1" extent="15mm" background-color="white" />
				</fo:simple-page-master>
				<fo:simple-page-master master-name="zweiteSeite" page-height="297mm" page-width="210mm">
					<fo:region-body region-name="Body" margin-top="57mm" margin-left="51mm" margin-right="10mm" margin-bottom="23mm" background-color="white" />
					<fo:region-before region-name="KopfSeite2" extent="57mm" precedence="true" background-color="white" />
					<fo:region-after region-name="FussSeite2" extent="23mm" background-color="white" />
					<fo:region-start region-name="LRandSeite2" extent="51mm" background-color="white" />
					<fo:region-end region-name="RRandSeite2" extent="10mm" background-color="white" />
				</fo:simple-page-master>
				<fo:page-sequence-master master-name="global">
					<fo:single-page-master-reference master-reference="ersteSeite" />
					<fo:repeatable-page-master-reference master-reference="zweiteSeite" />
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

				<fo:static-content flow-name="KopfSeite2">
					<fo:block-container position="absolute" left="51mm" top="0mm" height="33mm" width="41mm" border-bottom-style="solid" border-bottom-width="0.5pt">
						<fo:block padding-top="9mm">
							<fo:external-graphic src="{$imagePath}/image_siegel_Halle.gif" content-height="19mm" />
						</fo:block>
					</fo:block-container>
					<fo:block-container position="absolute" left="80mm" top="38mm" height="6mm" width="6mm" background-color="#{$viereckColor}">
						<fo:block />
					</fo:block-container>
					<fo:block-container position="absolute" left="92mm" top="0mm" height="33mm" width="118mm" border-left-style="solid" border-bottom-style="solid" border-left-width="0.5pt" border-bottom-width="0.5pt">
						<fo:block padding-top="12mm" margin-left="5mm" font-family="{$font}" font-size="20pt">
							MARTIN-LUTHER-UNIVERSITÄT
						</fo:block>
						<fo:block margin-left="5mm" font-family="{$font}" font-size="20pt">
							HALLE-WITTENBERG
						</fo:block>
					</fo:block-container>
					<!-- name der fakultät und des bereichs -->
					<fo:block-container position="absolute" left="92mm" top="33mm" height="24mm" width="118mm" border-left-style="solid" border-left-width="0.5pt">
						<fo:block padding-top="5mm" margin-left="5mm" font-family="{$font}" font-size="16pt" hyphenate="true" language="de" margin-right="15mm">
							<xsl:value-of select="Person/MyInfos/Einrichtung/ltxt" />
						</fo:block>
					</fo:block-container>
				</fo:static-content>

				<!-- Falzmarken immer am linken Rand -->
				<fo:static-content flow-name="LRandSeite1">
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
				
				<fo:static-content flow-name="LRandSeite2">
					<fo:block-container absolute-position="fixed" top="110mm" left="0mm" height="51mm" width="164mm" reference-orientation="90">
						<fo:block padding-top="15mm" font-family="{$font}" font-size="85pt" color="#b3b3b3">
							<!--xsl:if test="$printLang='en'">
								Certificate
							</xsl:if>
							<xsl:if test="$printLang='de'">
								Zertifikat
							</xsl:if-->
						</fo:block>
					</fo:block-container>
				</fo:static-content>

				<fo:static-content flow-name="FussSeite1">
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
					<!-- Anschreiben -->
					<fo:block-container absolute-position="fixed" top="119mm" left="30mm" width="170mm">
						<fo:block color="#000000" text-align="start" font-family="{$font}" font-weight="bold">
							Bescheinigung über erbrachte Leistungen im Promotionsverfahren
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
							im Auftrag 
							<xsl:choose>
								<xsl:when test="Person/Form/Rolle = 'Din'">der Dekanin </xsl:when>
								<xsl:when test="Person/Form/Rolle = 'D'">des Dekans </xsl:when>
								<xsl:when test="Person/Form/Rolle = 'Dkin'">der Geschäftsführenden Direktorin </xsl:when>
								<xsl:when test="Person/Form/Rolle = 'Dk'">des Geschäftsführenden Direktors </xsl:when>
							</xsl:choose>
							übersende ich Ihnen mit der Anlage die Bescheinigung über die erbrachten Leistungen in Ihrem Promotionsverfahren.
							<fo:block /> 
							Herzlichen Glückwunsch! 
						</fo:block>
						<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="de" padding-top="1em">
							Für Ihre weitere Tätigkeit wünsche ich Ihnen viel Erfolg und persönlich alles Gute.
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" padding-top="2em">
							Mit freundlichen Grüßen
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" padding-top="7em">
							<xsl:if test="Person/Form/Titel">
								<xsl:value-of select="Person/Form/Titel" /><xsl:text> </xsl:text>
							</xsl:if>
							<xsl:if test="Person/Form/Vorname">
								<xsl:value-of select="Person/Form/Vorname" /><xsl:text> </xsl:text>
							</xsl:if>
							<xsl:value-of select="Person/Form/Nachname" />
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}">
							<xsl:if test="$printLang='en'">
								<xsl:choose>
									<xsl:when test="Person/Form/Rolle = 'Din'">Dekanin</xsl:when>
									<xsl:when test="Person/Form/Rolle = 'D'">Dekan</xsl:when>
									<xsl:when test="Person/Form/Rolle = 'Dkin'">Geschäftsführende Direktorin </xsl:when>
									<xsl:when test="Person/Form/Rolle = 'Dk'">Geschäftsführender Direktor</xsl:when>
								</xsl:choose>
							</xsl:if>
							<xsl:if test="$printLang='de'">
								<xsl:choose>
									<xsl:when test="Person/Form/Rolle = 'Din'">Dekanin</xsl:when>
									<xsl:when test="Person/Form/Rolle = 'D'">Dekan</xsl:when>
									<xsl:when test="Person/Form/Rolle = 'Dkin'">Geschäftsführende Direktorin </xsl:when>
									<xsl:when test="Person/Form/Rolle = 'Dk'">Geschäftsführender Direktor</xsl:when>
								</xsl:choose>
							</xsl:if>
<!--
							<xsl:value-of select="Person/MyInfos/PerVor" /><xsl:text> </xsl:text><xsl:value-of select="Person/MyInfos/PerNac" />
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}">
							<xsl:if test="Person/MyInfos/Geschl='M'">
								<xsl:text>Promotionsbeauftragter</xsl:text>
							</xsl:if>
							<xsl:if test="Person/MyInfos/Geschl='W'">
								<xsl:text>Promotionsbeauftragte</xsl:text>
							</xsl:if>
-->
						</fo:block>
					</fo:block-container>
					<fo:block break-after="page" />
					
					<!-- Zertifikat -->
					<fo:block-container margin-top="5mm" width="149mm" background-color="white">
						<!--fo:block color="#000000" margin-left="5mm" margin-right="5mm" padding-top="5em" text-align="center" font-family="{$font}" font-size="12pt" hyphenate="true" language="de" font-weight="bold">
							<xsl:value-of select="Person/MyInfos/Einrichtung/ltxt" />
						</fo:block>
						<fo:block color="#000000" margin-left="5mm" margin-right="5mm" padding-top="3em" font-family="{$font}" font-size="14pt" text-align="center" hyphenate="true" language="{$printLang}" font-weight="bold">
							<xsl:if test="$printLang='en'">
								Certificate
							</xsl:if>
							<xsl:if test="$printLang='de'">
								Zertifikat
							</xsl:if>
						</fo:block-->
						<fo:block color="#000000" margin-left="5mm" margin-right="5mm" padding-top="10em" font-family="{$font}" font-size="11pt" text-align="center" hyphenate="true" language="{$printLang}">
							<xsl:if test="$printLang='en'">
								<xsl:if test="Person/Geschl='M'">Mr.</xsl:if>
								<xsl:if test="Person/Geschl='W'">Ms.</xsl:if>
							</xsl:if>
							<xsl:if test="$printLang='de'">
								<xsl:if test="Person/Geschl='M'">Herr</xsl:if>
								<xsl:if test="Person/Geschl='W'">Frau</xsl:if>
							</xsl:if>
						</fo:block>
						<fo:block color="#000000" margin-left="5mm" margin-right="5mm" padding-top="1em" font-family="{$font}" font-size="11pt" text-align="center" hyphenate="true" language="{$printLang}" font-weight="bold">
								<xsl:value-of select="Person/PerVor" /><xsl:text> </xsl:text><xsl:value-of select="Person/PerNac" />
						</fo:block>
						<fo:block color="#000000" margin-left="5mm" margin-right="5mm" padding-top="1em" font-family="{$font}" font-size="11pt" text-align="center" hyphenate="true" language="{$printLang}">
							<xsl:if test="$printLang='en'">
								born at <xsl:value-of select="Person/GebDat" /> in <xsl:value-of select="Person/GebOrt" />
							</xsl:if>
							<xsl:if test="$printLang='de'">
								geboren am <xsl:value-of select="Person/GebDat" /> in <xsl:value-of select="Person/GebOrt" />
							</xsl:if>
						</fo:block>
						<fo:block color="#000000" margin-left="5mm" margin-right="5mm" padding-top="1em" font-family="{$font}" font-size="11pt" text-align="center" hyphenate="true" language="{$printLang}">
							<xsl:if test="$printLang='en'">
								has proven <xsl:if test="Person/Geschl='M'">his</xsl:if><xsl:if test="Person/Geschl='W'">her</xsl:if> scientific qualification in a conferral of a doctorate in accordance with the rules for the obtainment of the academic degree
							</xsl:if>
							<xsl:if test="$printLang='de'">
								hat im Rahmen <xsl:if test="Person/Geschl='M'">seines</xsl:if><xsl:if test="Person/Geschl='W'">ihres</xsl:if> ordnungsgemäßen Promotionsverfahrens zur Erlangung des akademischen Grades
							</xsl:if>
						</fo:block>
						<fo:block color="#000000" margin-left="5mm" margin-right="5mm" padding-top="1em" font-family="{$font}" font-size="11pt" text-align="center" hyphenate="true" language="{$printLang}" font-weight="bold">
								<xsl:value-of select="Person/MyPromo/AkadGrad" />
						</fo:block>
						<fo:block color="#000000" margin-left="5mm" margin-right="5mm" padding-top="1em" font-family="{$font}" font-size="11pt" text-align="center" hyphenate="true" language="{$printLang}">
							<xsl:if test="$printLang='en'">
								with a dissertation on
							</xsl:if>
							<xsl:if test="$printLang='de'">
								<xsl:if test="Person/Geschl='M'">mit seiner Dissertation</xsl:if>
								<xsl:if test="Person/Geschl='W'">mit ihrer Dissertation</xsl:if>
							</xsl:if>
						</fo:block>
						<fo:block color="#000000" margin-left="5mm" margin-right="5mm" padding-top="1em" font-family="{$font}" font-size="11pt" text-align="center" hyphenate="true" language="{$printLang}" font-weight="bold">
								"<xsl:value-of select="Person/MyPromo/Titel" />"
						</fo:block>
						<fo:block color="#000000" margin-left="5mm" margin-right="5mm" padding-top="1em" font-family="{$font}" font-size="11pt" text-align="center" hyphenate="true" language="{$printLang}">
							<xsl:if test="$printLang='en'">
								and a public defense on <xsl:value-of select="Person/MyPromo/Process/dateOfColloquium" /> and has got the appraisal
							</xsl:if>
							<xsl:if test="$printLang='de'">
								<xsl:if test="Person/Geschl='M'">und einer öffentlichen Verteidigung am <xsl:value-of select="Person/MyPromo/Process/dateOfColloquium" /> seine wissenschaftliche Befähigung nachgewiesen und dabei die Gesamtbewertung</xsl:if>
								<xsl:if test="Person/Geschl='W'">und einer öffentlichen Verteidigung am <xsl:value-of select="Person/MyPromo/Process/dateOfColloquium" /> ihre wissenschaftliche Befähigung nachgewiesen und dabei die Gesamtbewertung</xsl:if>
							</xsl:if>
						</fo:block>
						<xsl:variable name="pnote">
							<xsl:for-each select="Person/Gradings/Grading">
								<xsl:if test="Key='note'"><xsl:value-of select="BewertungID" /></xsl:if>
							</xsl:for-each>
						</xsl:variable>
						<fo:block color="#000000" margin-left="5mm" margin-right="5mm" padding-top="1em" font-family="{$font}" font-size="11pt" text-align="center" hyphenate="true" language="{$printLang}" font-weight="bold">
							<xsl:if test="$pnote != '' and $pnote != ' '">
								<xsl:value-of select="concat(substring($pnote,1,1),',',substring($pnote,2,1))" />
							</xsl:if>
						</fo:block>
						<fo:block color="#000000" margin-left="5mm" margin-right="5mm" padding-top="0em" font-family="{$font}" font-size="11pt" text-align="center" hyphenate="true" language="{$printLang}">
							(<xsl:for-each select="Person/Gradings/Grading">
								<xsl:if test="Key='ges'"><xsl:value-of select="Bewertung" /></xsl:if>
							</xsl:for-each>)<xsl:if test="$printLang='en'">.</xsl:if>
						</fo:block>
						<fo:block color="#000000" margin-left="5mm" margin-right="5mm" padding-top="1em" font-family="{$font}" font-size="11pt" text-align="center" hyphenate="true" language="{$printLang}">
							<xsl:if test="$printLang='de'">
								erhalten.
							</xsl:if>
						</fo:block>
						<fo:block color="#000000" margin-left="5mm" margin-right="5mm" padding-top="1em" font-family="{$font}" font-size="11pt" text-align="center" hyphenate="true" language="{$printLang}">
							<xsl:if test="$printLang='en'">
								It is necessary yet to deliver the duty copies of the dissertation, the procedure of conferral of a doctorate is <fo:inline font-weight="bold">not</fo:inline> yet finished. This certificate will lose the validity after handing over the candidate the official promotion document, but at the latest after one year.
							</xsl:if>
							<xsl:if test="$printLang='de'">
								Die Abgabe der Pflichtexemplare der Dissertation steht noch aus, das Promotionsverfahren ist also noch <fo:inline font-weight="bold">nicht</fo:inline> abgeschlossen. Diese Bescheinigung verliert ihre Gültigkeit, wenn
								<xsl:if test="Person/Geschl='M'">dem Kandidaten </xsl:if>
								<xsl:if test="Person/Geschl='W'">der Kandidatin </xsl:if>
								die Promotionsurkunde überreicht wurde, spätestens jedoch ein Jahr nach dem Tag der öffentlichen Verteidigung.
							</xsl:if>
						</fo:block>
						<fo:block color="#000000" margin-left="5mm" margin-right="5mm" padding-top="1em" font-family="{$font}" font-size="11pt" text-align="start" hyphenate="true" language="{$printLang}">
							<xsl:if test="$printLang='en'">
								Halle, <xsl:value-of select="date:format-date($now,'dd.MM.yyyy')"/>
							</xsl:if>
							<xsl:if test="$printLang='de'">
								Halle, den <xsl:value-of select="date:format-date($now,'dd.MM.yyyy')"/>
							</xsl:if>
						</fo:block>
					</fo:block-container>
					<fo:block-container position="absolute" top="207.5mm" width="149mm">
						<fo:block color="#000000" margin-left="5mm" margin-right="5mm" text-align="start" font-family="{$font}" font-size="11pt">
							<xsl:if test="Person/Form/Titel">
								<xsl:value-of select="Person/Form/Titel" /><xsl:text> </xsl:text>
							</xsl:if>
							<xsl:if test="Person/Form/Vorname">
								<xsl:value-of select="Person/Form/Vorname" /><xsl:text> </xsl:text>
							</xsl:if>
							<xsl:value-of select="Person/Form/Nachname" />
						</fo:block>
						<fo:block color="#000000" margin-left="5mm" margin-right="5mm" padding-top="0em" font-family="{$font}" font-size="11pt" text-align="start">
							<xsl:if test="$printLang='en'">
								<xsl:choose>
									<xsl:when test="Person/Form/Rolle = 'Din'">Dekanin</xsl:when>
									<xsl:when test="Person/Form/Rolle = 'D'">Dekan</xsl:when>
									<xsl:when test="Person/Form/Rolle = 'Dkin'">Geschäftsführende Direktorin </xsl:when>
									<xsl:when test="Person/Form/Rolle = 'Dk'">Geschäftsführender Direktor</xsl:when>
								</xsl:choose>
							</xsl:if>
							<xsl:if test="$printLang='de'">
								<xsl:choose>
									<xsl:when test="Person/Form/Rolle = 'Din'">Dekanin</xsl:when>
									<xsl:when test="Person/Form/Rolle = 'D'">Dekan</xsl:when>
									<xsl:when test="Person/Form/Rolle = 'Dkin'">Geschäftsführende Direktorin </xsl:when>
									<xsl:when test="Person/Form/Rolle = 'Dk'">Geschäftsführender Direktor</xsl:when>
								</xsl:choose>
							</xsl:if>
						</fo:block>
					</fo:block-container>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>
