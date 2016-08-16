<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:date="http://exslt.org/dates-and-times" version="1.0">
	<!--xsl:import href="[TEMPLATEROOT]format.xsl" /-->
	<xsl:output method="xml" indent="yes" />
	<xsl:variable name="now" select="date:date-time()" />
	<xsl:preserve-space elements="*" />
	<xsl:preserve-space elements="VerComment" />

	<!-- Root-Einsprungspunkt Seitendefinition xsl:template match muss auf das Root-Element der XML-Datei angepaßt werden -->
	<xsl:template match="publishDetail">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<!-- Seitengröße inkl. der Ränder festlegen -->
				<fo:simple-page-master master-name="simple" 
					page-height="29.0cm" page-width="21.5cm" 
					margin-top="1.0cm" margin-bottom="1.0cm" 
					margin-left="2.5cm" margin-right="2cm">
					<fo:region-body margin-top="1cm" />
					<fo:region-before extent="1cm" />
					<fo:region-after extent="0.7cm" />
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="simple" language="de" hyphenate="true">
				<!-- Kopfzeile der Seite definieren mit Inhalten füllen -->
				<fo:static-content flow-name="xsl-region-before">
					<fo:block></fo:block>
				</fo:static-content>
				<!-- ENDE der Kopfzeilendefinition -->

				<!-- Fußzeile der Seite definieren mit Inhalten füllen -->
				<fo:static-content flow-name="xsl-region-after">
					<fo:block space-before="10pt">
						<fo:leader leader-pattern="rule" rule-thickness="0.5pt" leader-length="18.5cm" />
					</fo:block>
					<!-- Seiteninformation -->
					<fo:block-container height="1cm" width="2cm" left="16.5cm" top="0.5cm" position="absolute">
						<fo:block text-align="right" font-size="6pt">
							Seite
							<fo:page-number />
							von
							<fo:page-number-citation ref-id="lastPage" />
						</fo:block>
					</fo:block-container>
				</fo:static-content>
				<!-- ENDE der Fußzeilendefinition -->

				<!-- Initialisieren des Haupt-Druckbereiches -->
				<fo:flow flow-name="xsl-region-body">
					<xsl:apply-templates />
					<fo:block id="lastPage" />
				</fo:flow>
				<!-- Initialisierung ENDE -->
			</fo:page-sequence>
		</fo:root>
	</xsl:template>

	<!-- Bereich zur Definition der Templates, die im Haupt-Druckbereich untergebracht werden sollen -->
	<xsl:template match="Promotion">
		<xsl:variable name="useLang">
			<xsl:value-of select="substring(/publishDetail/Contants/language,2,2)" />
		</xsl:variable>
		<!-- Aufbau der Tabelle für die Veranstaltung -->
		<fo:block id="Lecture" font-size="12pt">
			<fo:block font-weight="bold">
				<xsl:choose>
					<xsl:when test="normalize-space($useLang)='en'">
						<xsl:value-of select="/publishDetail/Contants/application1EN" /><xsl:text> ... </xsl:text><xsl:value-of select="/publishDetail/Contants/application2EN" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="/publishDetail/Contants/application1DE" /><xsl:text> ... </xsl:text><xsl:value-of select="/publishDetail/Contants/application2DE" />
					</xsl:otherwise>
				</xsl:choose>
			</fo:block>
			<fo:block margin-top="1em">
				<xsl:choose>
					<xsl:when test="normalize-space($useLang)='en'">
						<xsl:text>at the institute ...</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>im Institut ...</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</fo:block>
			<fo:block margin-top="1em">
				<xsl:choose>
					<xsl:when test="normalize-space($useLang)='en'">
						<xsl:value-of select="/publishDetail/Contants/formalRegsEN" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="/publishDetail/Contants/formalRegsDE" />
					</xsl:otherwise>
				</xsl:choose>
			</fo:block>
			<fo:block>
				<fo:block margin-top="2em" font-weight="bold">
					<xsl:choose>
						<xsl:when test="normalize-space($useLang)='en'"><xsl:text>Personal data:</xsl:text></xsl:when>
						<xsl:otherwise><xsl:text>Persönliche Daten:</xsl:text></xsl:otherwise>
					</xsl:choose>
				</fo:block>
				<fo:block margin-top="1em">
					<xsl:choose>
						<xsl:when test="normalize-space($useLang)='en'">
							<fo:block><xsl:text>Surname and name at birth: </xsl:text><xsl:value-of select="Person/PerNam" /><xsl:text> </xsl:text><xsl:value-of select="Person/PerNac" /></fo:block>
							<fo:block><xsl:text>First name: </xsl:text><xsl:value-of select="Person/PerVor" /></fo:block>
							<fo:block>
								<xsl:text>Gender: </xsl:text>
								<xsl:choose>
									<xsl:when test="Person/Geschl = 'M'"><xsl:text>male</xsl:text></xsl:when>
									<xsl:otherwise><xsl:text>female</xsl:text></xsl:otherwise>
								</xsl:choose>
							</fo:block>
							<fo:block><xsl:text>Date of birth: </xsl:text><xsl:value-of select="Person/GebDat" /></fo:block>
							<fo:block><xsl:text>Place of birth: </xsl:text><xsl:value-of select="Person/GebOrt" /></fo:block>
							<fo:block><xsl:text>Country of birth: </xsl:text><xsl:value-of select="Person/GebLand" /></fo:block>
							<fo:block><xsl:text>Nationality: </xsl:text><xsl:value-of select="Person/Nationality" /></fo:block>
							<fo:block margin-top="1em"><xsl:text>Present address: </xsl:text></fo:block>
							<fo:block><xsl:text>Street: </xsl:text><xsl:value-of select="Person/Adresse/Strasse" /></fo:block>
							<fo:block><xsl:text>Postalcode - City: </xsl:text><xsl:value-of select="Person/Adresse/PLZ" /><xsl:text> - </xsl:text><xsl:value-of select="Person/Adresse/Ort" /></fo:block>
							<fo:block><xsl:text>Country: </xsl:text><xsl:value-of select="Person/Adresse/Land" /></fo:block>
							<fo:block><xsl:text>Telephone number: </xsl:text><xsl:value-of select="Person/Adresse/Telefon" /></fo:block>
							<fo:block><xsl:text>Email-address: </xsl:text><xsl:value-of select="Person/Adresse/eMail" /></fo:block>
						</xsl:when>
						<xsl:otherwise>
							<fo:block><xsl:text>Name und Geburtsname: </xsl:text><xsl:value-of select="Person/PerNam" /><xsl:text> </xsl:text><xsl:value-of select="Person/PerNac" /></fo:block>
							<fo:block><xsl:text>Vorname: </xsl:text><xsl:value-of select="Person/PerVor" /></fo:block>
							<fo:block>
								<xsl:text>Geschlecht: </xsl:text>
								<xsl:choose>
									<xsl:when test="Person/Geschl = 'M'"><xsl:text>männlich</xsl:text></xsl:when>
									<xsl:otherwise><xsl:text>weiblich</xsl:text></xsl:otherwise>
								</xsl:choose>
							</fo:block>
							<fo:block><xsl:text>Geburtsdatum: </xsl:text><xsl:value-of select="Person/GebDat" /></fo:block>
							<fo:block><xsl:text>Geburtsort: </xsl:text><xsl:value-of select="Person/GebOrt" /></fo:block>
							<fo:block><xsl:text>Geburtsland: </xsl:text><xsl:value-of select="Person/GebLand" /></fo:block>
							<fo:block><xsl:text>Staatsangehörigkeit: </xsl:text><xsl:value-of select="Person/Nationality" /></fo:block>
							<fo:block margin-top="1em"><xsl:text>Wohnanschrift: </xsl:text></fo:block>
							<fo:block><xsl:text>Straße: </xsl:text><xsl:value-of select="Person/Adresse/Strasse" /></fo:block>
							<fo:block><xsl:text>PLZ - Ort: </xsl:text><xsl:value-of select="Person/Adresse/PLZ" /><xsl:text> - </xsl:text><xsl:value-of select="Person/Adresse/Ort" /></fo:block>
							<fo:block><xsl:text>Land: </xsl:text><xsl:value-of select="Person/Adresse/Land" /></fo:block>
							<fo:block><xsl:text>Telefonnummer: </xsl:text><xsl:value-of select="Person/Adresse/Telefon" /></fo:block>
							<fo:block><xsl:text>E-Mail-Adresse: </xsl:text><xsl:value-of select="Person/Adresse/eMail" /></fo:block>
						</xsl:otherwise>
					</xsl:choose>
				</fo:block>
				<fo:block margin-top="1em">
					<fo:table>
						<fo:table-column column-width="1.5cm"/>
						<fo:table-column column-width="16cm"/>
						<fo:table-body>
							<xsl:choose>
								<xsl:when test="normalize-space($useLang)='en'">
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2"><fo:block><xsl:text>Current status of the applicant</xsl:text></fo:block></fo:table-cell>
									</fo:table-row>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Scientific staff at the University of Halle (Budget)</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="Person/BewerberStatusKey = 'WMHH'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
									</xsl:call-template>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Scientific staff at the University of Halle (External funds)</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="Person/BewerberStatusKey = 'WMDM'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
									</xsl:call-template>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Scholarship holder</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="Person/BewerberStatusKey = 'STIP'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
									</xsl:call-template>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">External doctoral candidate</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="Person/BewerberStatusKey = 'EXT'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
									</xsl:call-template>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Others</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="Person/BewerberStatusKey = 'OTH'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<fo:table-row>
										<fo:table-cell number-columns-spanned="2"><fo:block><xsl:text>Aktueller Status der Antragstellerin bzw. des Antragsstellers</xsl:text></fo:block></fo:table-cell>
									</fo:table-row>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Wissenschaftliche Mitarbeiterin bzw. wissenschaftlicher Mitarbeiter der Universität Halle (Haushalt)</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="Person/BewerberStatusKey = 'WMHH'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
									</xsl:call-template>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Wissenschaftliche Mitarbeiterin bzw. wissenschaftlicher Mitarbeiter der Universität Halle (Drittmittel)</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="Person/BewerberStatusKey = 'WMDM'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
									</xsl:call-template>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Stipendiatin bzw. Stipendiat</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="Person/BewerberStatusKey = 'STIP'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
									</xsl:call-template>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Externe Doktorandin bzw. externer Doktorand</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="Person/BewerberStatusKey = 'EXT'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
									</xsl:call-template>
									<xsl:call-template name="checkField">
										<xsl:with-param name="text">Anderes</xsl:with-param>
										<xsl:with-param name="isChecked"><xsl:choose><xsl:when test="Person/BewerberStatusKey = 'OTH'">1</xsl:when><xsl:otherwise>0</xsl:otherwise></xsl:choose></xsl:with-param>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
						</fo:table-body>
					</fo:table>
				</fo:block>
				<fo:block margin-top="1em">
					<xsl:choose>
						<xsl:when test="normalize-space($useLang)='en'">
							<fo:block><xsl:text>An employment contract for the applicant is currently in force with:</xsl:text></fo:block>
							<fo:block margin-top="1em"><xsl:text>Name of institution: </xsl:text><xsl:value-of select="Person/Employment/Institution" /></fo:block>
							<fo:block>
								<xsl:text>Street / Postal code - City: </xsl:text>
								<xsl:value-of select="Person/Employment/Adresse/Strasse" />
								<xsl:text> / </xsl:text>
								<xsl:value-of select="Person/Employment/Adresse/PLZ" />
								<xsl:text> - </xsl:text>
								<xsl:value-of select="Person/Employment/Adresse/Ort" />
							</fo:block>
							<fo:block><xsl:text>Telephone number: </xsl:text><xsl:value-of select="Person/Employment/Adresse/Telefon" /></fo:block>
							<fo:block><xsl:text>Email address: </xsl:text><xsl:value-of select="Person/Employment/Adresse/eMail" /></fo:block>
							<fo:block><xsl:text>as: </xsl:text><xsl:value-of select="Person/Employment/Beschaeftigung" /></fo:block>
						</xsl:when>
						<xsl:otherwise>
							<fo:block><xsl:text>Ein Arbeitsrechtsverhältnis besteht zur Zeit der Antragsstellung mit:</xsl:text></fo:block>
							<fo:block margin-top="1em"><xsl:text>Name der Institution: </xsl:text><xsl:value-of select="Person/Employment/Institution" /></fo:block>
							<fo:block>
								<xsl:text>Straße / PLZ - Ort: </xsl:text>
								<xsl:value-of select="Person/Employment/Adresse/Strasse" />
								<xsl:text> / </xsl:text>
								<xsl:value-of select="Person/Employment/Adresse/PLZ" />
								<xsl:text> - </xsl:text>
								<xsl:value-of select="Person/Employment/Adresse/Ort" />
							</fo:block>
							<fo:block><xsl:text>Telefonnummer: </xsl:text><xsl:value-of select="Person/Employment/Adresse/Telefon" /></fo:block>
							<fo:block><xsl:text>E-Mail-Adresse: </xsl:text><xsl:value-of select="Person/Employment/Adresse/eMail" /></fo:block>
							<fo:block><xsl:text>als: </xsl:text><xsl:value-of select="Person/Employment/Beschaeftigung" /></fo:block>
						</xsl:otherwise>
					</xsl:choose>
				</fo:block>
			</fo:block>
		</fo:block>
	</xsl:template>
	<xsl:template name="checkField">
		<xsl:param name="text" />
		<xsl:param name="isChecked" />
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
	</xsl:template>
	
</xsl:stylesheet>

