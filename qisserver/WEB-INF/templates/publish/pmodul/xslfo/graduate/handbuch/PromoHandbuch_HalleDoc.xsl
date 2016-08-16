<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:date="http://exslt.org/dates-and-times" extension-element-prefixes="date"
	xmlns:svg="http://www.w3.org/2000/svg"
	xmlns:dyn="http://exslt.org/dynamic" version="1.0">

	<xsl:output method="xml" indent="yes" />
	<xsl:decimal-format name="FrenchDecimalFormat" decimal-separator="." grouping-separator="&#160;" />
	<xsl:decimal-format name="european" decimal-separator="." grouping-separator="," />
	<xsl:variable name="now" select="date:date-time()" />

	<xsl:template match="publishDetail">

		<xsl:variable name="imagePath">file:[TEMPLATEROOT]publish/pmodul/xslfo/graduate/handbuch</xsl:variable>
		<!--xsl:variable name="imagePath">D:/Tomcats/loewenportal170/webapps/portal/WEB-INF/templates/publish/pmodul/xslfo/graduate/handbuch</xsl:variable-->
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

				<fo:static-content flow-name="Fuss">
					<fo:block-container position="absolute" left="15mm" top="4mm">
						<fo:block />
					</fo:block-container>
				</fo:static-content>

				<fo:flow flow-name="Body" font-family="{$font}" font-size="10pt">
					<fo:block-container absolute-position="auto" break-before="auto">
						<fo:block color="#000000" text-align="justify" font-family="{$font}" font-size="14pt" font-weight="bold" padding-bottom="1em">
							Benutzerdokumentation - <fo:inline text-transform="capitalize">HalDoc</fo:inline>
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" font-weight="bold" font-size="12pt" padding-bottom="1em">1. Das Menü</fo:block>
						<fo:block color="#000000" text-align="justify" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							<fo:table table-layout="fixed" width="16.5cm">
								<fo:table-column column-width="7.5cm"/>
								<fo:table-column column-width="9cm"/>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell>
											<fo:block>
												<xsl:call-template name="insertImage">
													<xsl:with-param name="image"><xsl:value-of select="$imagePath" /><xsl:text>/Status.png</xsl:text></xsl:with-param>
													<xsl:with-param name="width">40mm</xsl:with-param>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block>
												Nach dem Anmelden im Löwenportal sehen Sie Ihre benutzbaren Menüpunkte. Sollte Ihnen das HalDoc-System noch nicht zur Verfügung stehen, schauen Sie bitte unterhalb des Bildes rechts in Ihrem Status, ob Sie die korrekte Rolle für die Promovierendenverwaltung innehaben.
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell>
											<fo:block>
												<xsl:call-template name="insertImage">
													<xsl:with-param name="image"><xsl:value-of select="$imagePath" /><xsl:text>/Menue.png</xsl:text></xsl:with-param>
													<xsl:with-param name="width">50mm</xsl:with-param>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell>
											<fo:block>
												<fo:list-block>
													<fo:list-item>
														<fo:list-item-label start-indent="5pt"><fo:block>&#x2022;</fo:block></fo:list-item-label>
														<fo:list-item-body start-indent="15pt">
															<fo:block><fo:inline font-weight="bold">Antragsteller/in bearbeiten:</fo:inline> alle bestehenden Anträge sehen und bearbeiten</fo:block>
														</fo:list-item-body>
													</fo:list-item>
													<fo:list-item>
														<fo:list-item-label start-indent="5pt"><fo:block>&#x2022;</fo:block></fo:list-item-label>
														<fo:list-item-body start-indent="15pt">
															<fo:block><fo:inline font-weight="bold">Neue Promotion:</fo:inline> neue Promotion händisch eingeben</fo:block>
														</fo:list-item-body>
													</fo:list-item>
													<fo:list-item>
														<fo:list-item-label start-indent="5pt"><fo:block>&#x2022;</fo:block></fo:list-item-label>
														<fo:list-item-body start-indent="15pt">
															<fo:block><fo:inline font-weight="bold">Doktorand/in bearbeiten:</fo:inline> angenommene Doktoranden und Promotionen bearbeiten</fo:block>
														</fo:list-item-body>
													</fo:list-item>
													<fo:list-item>
														<fo:list-item-label start-indent="5pt"><fo:block>&#x2022;</fo:block></fo:list-item-label>
														<fo:list-item-body start-indent="15pt">
															<fo:block><fo:inline font-weight="bold">Personen bearbeiten (Sachbearbeiter):</fo:inline> Sachbearbeiter für die Doktoranden bearbeiten und neue anlegen</fo:block>
														</fo:list-item-body>
													</fo:list-item>
													<fo:list-item>
														<fo:list-item-label start-indent="5pt"><fo:block>&#x2022;</fo:block></fo:list-item-label>
														<fo:list-item-body start-indent="15pt">
															<fo:block><fo:inline font-weight="bold">Personen bearbeiten (Gutachter / Betreuer):</fo:inline> Gutachter und Betreuer eingeben und bearbeiten</fo:block>
														</fo:list-item-body>
													</fo:list-item>
													<fo:list-item>
														<fo:list-item-label start-indent="5pt"><fo:block>&#x2022;</fo:block></fo:list-item-label>
														<fo:list-item-body start-indent="15pt">
															<fo:block><fo:inline font-weight="bold">Suche nach Promotionen:</fo:inline> Übersicht der Promotionen mit Suche</fo:block>
														</fo:list-item-body>
													</fo:list-item>
													<fo:list-item>
														<fo:list-item-label start-indent="5pt"><fo:block>&#x2022;</fo:block></fo:list-item-label>
														<fo:list-item-body start-indent="15pt">
															<fo:block><fo:inline font-weight="bold">Übersichtsstatistik anzeigen:</fo:inline> Hochschulweite Statistiken zu den Promotionen</fo:block>
														</fo:list-item-body>
													</fo:list-item>
												</fo:list-block>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</fo:table-body>
							</fo:table>
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" font-weight="bold" font-size="12pt" padding-bottom="1em">2. Anträge</fo:block>
						<fo:block color="#000000" text-align="justify" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							Mit dem Menüpunkt <fo:inline font-weight="bold">"Antragsteller/in bearbeiten"</fo:inline> öffnet sich eine Suchmaske, mit welcher bestehende Anträge gesucht werden können. Wenn keine Suchkriterien eingegeben werden, werden alle Anträge der eigenen Fakultät angezeigt.
						</fo:block>
						<fo:block color="#000000" text-align="center" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							<xsl:call-template name="insertImage">
								<xsl:with-param name="image"><xsl:value-of select="$imagePath" /><xsl:text>/Bewerbungen_01.png</xsl:text></xsl:with-param>
								<xsl:with-param name="width">160mm</xsl:with-param>
							</xsl:call-template>
						</fo:block>
						<fo:block color="#000000" text-align="center" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							<xsl:call-template name="insertImage">
								<xsl:with-param name="image"><xsl:value-of select="$imagePath" /><xsl:text>/Bewerbungen_02.png</xsl:text></xsl:with-param>
								<xsl:with-param name="width">160mm</xsl:with-param>
							</xsl:call-template>
						</fo:block>
						<fo:block color="#000000" text-align="justify" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							In der Suchergebnisliste gibt es grundlegende Informationen, mit welchen Sie die Anträge identifizieren können. In der ersten Spalte finden Sie zwei Aktionen:
						</fo:block>
						<fo:block color="#000000" text-align="justify" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							<xsl:variable name="img"><xsl:value-of select="$imagePath" /><xsl:text>/Bewerbungen_01_1.png</xsl:text></xsl:variable>
							<fo:external-graphic src="{$img}" content-width="10pt" padding="0pt"  /> Anzeigen von weiteren Informationen und Aktionen wie <fo:inline font-weight="bold">Ablehnung drucken</fo:inline> oder <fo:inline font-weight="bold">Antrag annehmen</fo:inline>
						</fo:block>
						<fo:block color="#000000" text-align="justify" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							<xsl:variable name="img"><xsl:value-of select="$imagePath" /><xsl:text>/Bewerbungen_01_2.png</xsl:text></xsl:variable>
							<fo:external-graphic src="{$img}" content-width="10pt" padding="0pt"  /> Antrag bearbeiten
						</fo:block>
						<fo:block color="#000000" text-align="justify" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							Sollte ein Antragsteller mehrere bisherige Abschlüsse angegeben haben, müssen Sie den für die Annahme relevanten Abschluss auswählen. Nach der Annahme sehen Sie eine Übersichtsseite in einem neuen Tab im Browser und können Sie die Promotion weiter bearbeiten. Ansonsten schließen Sie den Tab einfach und arbeiten mit dem nächsten Antrag weiter. Um die angenommenen Anträge aus der Liste zu entfernen, müssen Sie lediglich die Seite mit der Ergebnisliste neu laden.
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" font-weight="bold" font-size="12pt" padding-bottom="1em">3. Doktoranden</fo:block>
						<fo:block color="#000000" text-align="justify" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							Um bestehende Promotionen und die dazugehörigen Doktoranden bearbeiten zu können, wählen Sie den Menüpunkt <fo:inline font-weight="bold">"Doktorand/in bearbeiten"</fo:inline>. Auch hier gibt es eine Suchmaske, mit welcher Suchkriterien eingegeben werden können. Nach der Suche erhalten Sie eine Ergebnisliste mit grundlegenden Informationen. Über den Button <fo:inline font-weight="bold">"bearbeiten"</fo:inline> können Sie den Fall öffnen.
						</fo:block>
						<fo:block color="#000000" text-align="center" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							<xsl:call-template name="insertImage">
								<xsl:with-param name="image"><xsl:value-of select="$imagePath" /><xsl:text>/Doktorand_bearbeiten_01.png</xsl:text></xsl:with-param>
								<xsl:with-param name="width">160mm</xsl:with-param>
							</xsl:call-template>
						</fo:block>
						<fo:block color="#000000" text-align="justify" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							Wenn Sie einen Fall geöffnet haben, bekommen Sie mehrere Registerkarten zu einem Promovierenden angeboten. Hierzu gehören:
							<fo:block>
								<fo:list-block>
									<fo:list-item>
										<fo:list-item-label start-indent="5pt"><fo:block>&#x2022;</fo:block></fo:list-item-label>
										<fo:list-item-body start-indent="15pt">
											<fo:block><fo:inline font-weight="bold">Person:</fo:inline> Personengrunddaten des Promovierenden</fo:block>
										</fo:list-item-body>
									</fo:list-item>
									<fo:list-item>
										<fo:list-item-label start-indent="5pt"><fo:block>&#x2022;</fo:block></fo:list-item-label>
										<fo:list-item-body start-indent="15pt">
											<fo:block><fo:inline font-weight="bold">Kontaktdaten:</fo:inline> Kontaktinformationen des Promovierenden</fo:block>
										</fo:list-item-body>
									</fo:list-item>
									<fo:list-item>
										<fo:list-item-label start-indent="5pt"><fo:block>&#x2022;</fo:block></fo:list-item-label>
										<fo:list-item-body start-indent="15pt">
											<fo:block><fo:inline font-weight="bold">Promotion:</fo:inline> Informationen zur Promotion</fo:block>
										</fo:list-item-body>
									</fo:list-item>
									<fo:list-item>
										<fo:list-item-label start-indent="5pt"><fo:block>&#x2022;</fo:block></fo:list-item-label>
										<fo:list-item-body start-indent="15pt">
											<fo:block><fo:inline font-weight="bold">zusätzliche Informationen:</fo:inline> zusätzliche Informationen für Druckerzeugnisse, wie z.B. Auflagen für die Annahme oder Ablehnungsgründe</fo:block>
										</fo:list-item-body>
									</fo:list-item>
									<fo:list-item>
										<fo:list-item-label start-indent="5pt"><fo:block>&#x2022;</fo:block></fo:list-item-label>
										<fo:list-item-body start-indent="15pt">
											<fo:block><fo:inline font-weight="bold">Bewertungen:</fo:inline> Hier können die Bewertungskriterien zu einer Promotion hinterlegt werden. Da in jeder Fakultät andere Bewertungskriterien bestehen, kann diese Maske bei Ihnen eine andere Erscheinung haben.</fo:block>
										</fo:list-item-body>
									</fo:list-item>
									<fo:list-item>
										<fo:list-item-label start-indent="5pt"><fo:block>&#x2022;</fo:block></fo:list-item-label>
										<fo:list-item-body start-indent="15pt">
											<fo:block><fo:inline font-weight="bold">Ablauf:</fo:inline> Hier können alle Meilensteine einer Promotion hinterlegt werden, welche Sie für Ihre Prozesse und Abläufe benötigen.</fo:block>
										</fo:list-item-body>
									</fo:list-item>
									<fo:list-item>
										<fo:list-item-label start-indent="5pt"><fo:block>&#x2022;</fo:block></fo:list-item-label>
										<fo:list-item-body start-indent="15pt">
											<fo:block><fo:inline font-weight="bold">Abbruchinformationen:</fo:inline> Diese Registerkarte erscheint erst, wenn Sie im Ablauf ein Abbruchdatum eingetragen haben. In diesem Fall können Sie hier nun mehrere Abbruchgründe (fachliche und/oder private) hinterlegen.</fo:block>
										</fo:list-item-body>
									</fo:list-item>
								</fo:list-block>
							</fo:block>
						</fo:block>
						<fo:block color="#000000" text-align="center" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							<fo:table table-layout="fixed" width="16.5cm">
								<fo:table-column column-width="8cm"/>
								<fo:table-column column-width="0.5cm"/>
								<fo:table-column column-width="8cm"/>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell>
											<fo:block>
												<xsl:call-template name="insertImage">
													<xsl:with-param name="image"><xsl:value-of select="$imagePath" /><xsl:text>/Doktorand_bearbeiten_02.png</xsl:text></xsl:with-param>
													<xsl:with-param name="width">78mm</xsl:with-param>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell><fo:block /></fo:table-cell>
										<fo:table-cell>
											<fo:block>
												<xsl:call-template name="insertImage">
													<xsl:with-param name="image"><xsl:value-of select="$imagePath" /><xsl:text>/Doktorand_bearbeiten_03.png</xsl:text></xsl:with-param>
													<xsl:with-param name="width">78mm</xsl:with-param>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell>
											<fo:block>
												<xsl:call-template name="insertImage">
													<xsl:with-param name="image"><xsl:value-of select="$imagePath" /><xsl:text>/Doktorand_bearbeiten_04.png</xsl:text></xsl:with-param>
													<xsl:with-param name="width">78mm</xsl:with-param>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell><fo:block /></fo:table-cell>
										<fo:table-cell>
											<fo:block>
												<fo:block>
													<xsl:call-template name="insertImage">
														<xsl:with-param name="image"><xsl:value-of select="$imagePath" /><xsl:text>/Doktorand_bearbeiten_05.png</xsl:text></xsl:with-param>
														<xsl:with-param name="width">78mm</xsl:with-param>
													</xsl:call-template>
												</fo:block>
												<fo:block>
													<xsl:call-template name="insertImage">
														<xsl:with-param name="image"><xsl:value-of select="$imagePath" /><xsl:text>/Doktorand_bearbeiten_06.png</xsl:text></xsl:with-param>
														<xsl:with-param name="width">78mm</xsl:with-param>
													</xsl:call-template>
												</fo:block>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell>
											<fo:block>
												<xsl:call-template name="insertImage">
													<xsl:with-param name="image"><xsl:value-of select="$imagePath" /><xsl:text>/Doktorand_bearbeiten_07.png</xsl:text></xsl:with-param>
													<xsl:with-param name="width">78mm</xsl:with-param>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell><fo:block /></fo:table-cell>
										<fo:table-cell>
											<fo:block>
												<fo:block>
													<xsl:call-template name="insertImage">
														<xsl:with-param name="image"><xsl:value-of select="$imagePath" /><xsl:text>/Doktorand_bearbeiten_08.png</xsl:text></xsl:with-param>
														<xsl:with-param name="width">78mm</xsl:with-param>
													</xsl:call-template>
												</fo:block>
												<fo:block>
													<xsl:call-template name="insertImage">
														<xsl:with-param name="image"><xsl:value-of select="$imagePath" /><xsl:text>/Doktorand_bearbeiten_09.png</xsl:text></xsl:with-param>
														<xsl:with-param name="width">78mm</xsl:with-param>
													</xsl:call-template>
												</fo:block>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</fo:table-body>
							</fo:table>
						</fo:block>
						<fo:block color="#000000" text-align="justify" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							Auf der Registerkarte "Promotion" fallen die Felder auf, welche grün hinterlegt sind. Diese Felder sind eigentlich Auswahllisten, welche aber ausgeblendet werden. Wenn Sie einen neuen Eintrag hinterlegen möchten, dann haben Sie folgende Möglichkeiten:
							<fo:list-block>
								<fo:list-item>
									<fo:list-item-label end-indent="label-end()"><fo:block>1.</fo:block></fo:list-item-label>
									<fo:list-item-body start-indent="body-start()">
										<fo:block>Sie geben ein "?" ein und speichern, um die vollständige Liste zu erhalten</fo:block>
									</fo:list-item-body>
								</fo:list-item>
								<fo:list-item>
									<fo:list-item-label end-indent="label-end()"><fo:block>2.</fo:block></fo:list-item-label>
									<fo:list-item-body start-indent="body-start()">
										<fo:block>Sie geben einen Teil eines Eintrages ein, um alle Einträge mit diesem Teil zu erhalten. z.B. suchen Sie einen Betreuer und kennen nur dessen Nachnamen, denn geben Sie diesen Nachnamen ein, speichern und erhalten entweder:</fo:block>
										<fo:block>
											<fo:list-block provisional-distance-between-starts="10mm">
												<fo:list-item>
													<fo:list-item-label end-indent="label-end()"><fo:block>1.</fo:block></fo:list-item-label>
													<fo:list-item-body start-indent="body-start()">
														<fo:block>eine Liste aller Personen, welche diesen Nachnamen besitzen</fo:block>
													</fo:list-item-body>
												</fo:list-item>
												<fo:list-item>
													<fo:list-item-label end-indent="label-end()"><fo:block>2.</fo:block></fo:list-item-label>
													<fo:list-item-body start-indent="body-start()">
														<fo:block>wenn nur ein Eintrag ermittelt wurde, wird dieser direkt ausgewählt und gespeichert</fo:block>
													</fo:list-item-body>
												</fo:list-item>
												<fo:list-item>
													<fo:list-item-label end-indent="label-end()"><fo:block>3.</fo:block></fo:list-item-label>
													<fo:list-item-body start-indent="body-start()">
														<fo:block>wenn keine Person gefunden werden konnte erhalten Sie die gesamte Liste</fo:block>
													</fo:list-item-body>
												</fo:list-item>
											</fo:list-block>
										</fo:block>
									</fo:list-item-body>
								</fo:list-item>
							</fo:list-block>
						</fo:block>
						<fo:block color="#000000" text-align="justify" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							Bei den Betreuern und Gutachtern ist zu bedenken: Wenn Sie eine Person nicht finden sollten, dann müssen Sie den Dialog des Promovierenden verlassen und erst einen neuen Betreuer oder Gutachter über den Menüpunkt <fo:inline font-weight="bold">"Personen bearbeiten (Gutachter / Betreuer)"</fo:inline> anlegen.
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" font-weight="bold" font-size="12pt" padding-bottom="1em">4. Detailseiten eines Doktoranden</fo:block>
						<fo:block color="#000000" text-align="justify" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							<xsl:variable name="img"><xsl:value-of select="$imagePath" /><xsl:text>/Doktorand_bearbeiten_10.png</xsl:text></xsl:variable>
							Auf jeder Registerkarte sehen Sie einen Link
							<xsl:call-template name="insertImage">
								<xsl:with-param name="image"><xsl:value-of select="$imagePath" /><xsl:text>/Doktorand_bearbeiten_10.png</xsl:text></xsl:with-param>
								<xsl:with-param name="width">20mm</xsl:with-param>
							</xsl:call-template>. Mit diesem Link kommen Sie immer auf die Übersichtsseite des Promovierenden. Ebenso kommen Sie über den Menüpunkt <fo:inline font-weight="bold">"Suche nach Promotionen"</fo:inline> nach einer erfolgreichen Suche auf eine Suchergebnisliste und von dort auf diese Detailseite.
						</fo:block>
						<fo:block color="#000000" text-align="center" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							<xsl:call-template name="insertImage">
								<xsl:with-param name="image"><xsl:value-of select="$imagePath" /><xsl:text>/Doktorand_bearbeiten_11.png</xsl:text></xsl:with-param>
								<xsl:with-param name="width">160mm</xsl:with-param>
							</xsl:call-template>
						</fo:block>
						<fo:block color="#000000" text-align="justify" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							Auf der Detailseite sehen Sie alle hinterlegten Informationen zu dem geöffneten Fall auf einer Seite. Ebenso haben Sie hier einige Funktionen zur Verfügung, wenn Sie die Berechtigung hierzu haben:
						</fo:block>
						<fo:block color="#000000" text-align="justify" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							<xsl:variable name="img"><xsl:value-of select="$imagePath" /><xsl:text>/Doktorand_bearbeiten_11_1.png</xsl:text></xsl:variable>
							<fo:external-graphic src="{$img}" content-width="10pt" padding="0pt"  /> den angezeigten Fall zur Bearbeitung öffnen
						</fo:block>
						<fo:block color="#000000" text-align="justify" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							<xsl:variable name="img"><xsl:value-of select="$imagePath" /><xsl:text>/Doktorand_bearbeiten_11_2.png</xsl:text></xsl:variable>
							<fo:external-graphic src="{$img}" content-width="10pt" padding="0pt"  /> den angezeigten Fall zur Bearbeitung öffnen und direkt in die Registerkarte <fo:inline font-weight="bold">"Ablauf"</fo:inline> springen
						</fo:block>
						<fo:block color="#000000" text-align="justify" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							<xsl:variable name="img"><xsl:value-of select="$imagePath" /><xsl:text>/Doktorand_bearbeiten_11_3.png</xsl:text></xsl:variable>
							<fo:external-graphic src="{$img}" content-width="25mm" padding="0pt"  /> mit einem Klick auf den Pfeil erhalten Sie eine Liste von Druckerzeugnissen, welche Sie für die geöffnete Promotion erstellen können. Ggf. öffnen Sich ein kleines PopUp, um für das Druckerzeugnis zusätzliche (temporäre) Informationen eingeben zu können.
						</fo:block>
						<fo:block color="#000000" text-align="center" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							<xsl:call-template name="insertImage">
								<xsl:with-param name="image"><xsl:value-of select="$imagePath" /><xsl:text>/Doktorand_bearbeiten_12.png</xsl:text></xsl:with-param>
								<xsl:with-param name="width">160mm</xsl:with-param>
							</xsl:call-template>
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" font-weight="bold" font-size="12pt" padding-bottom="1em">5. Sachbearbeiter, Gutachter und Betreuer bearbeiten und neu eingeben</fo:block>
						<fo:block color="#000000" text-align="justify" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							Das Vorgehen und die Dialoge sind für all diese Personen identisch. Es gibt für Sachbearbeiter bzw. Gutachter und Betreuer zwei Menüpunkte, da so im Hintergrund das System die neu eingegebene Person einer dieser beiden Personengruppen automatisch zuordnen kann.
						</fo:block>
						<fo:block color="#000000" text-align="justify" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							Sie öffnen entsprechend der Personengruppe, für welche Sie eine neue Person anlegen oder eine bestehende bearbeiten wollen den passenden Menüpunkt <fo:inline font-weight="bold">"Personen bearbeiten"</fo:inline> und erhalten eine Suchmaske. Bitte suchen Sie immer erst, ob die Person bereits besteht, um Mehrfacheingaben zu verhindern. Für die Suche steht Ihnen eine Reihe von Wildcards zur Verfügung. Für mehr Informationen klicken Sie bitte in der Suchmaske auf den Link <fo:inline font-weight="bold">"Hilfe zur Suche"</fo:inline>.
						</fo:block>
						<fo:block color="#000000" text-align="justify" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							Wenn das System keine Person mit den eingegebenen Kriterien finden kann, wird Ihnen der Button <fo:inline font-weight="bold">"gewünschte Person nicht in Liste - Neu anlegen"</fo:inline> angeboten, über welchen Sie die gewünschte Person im System hinterlegen können.
						</fo:block>
						<fo:block color="#000000" text-align="justify" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							Es stehen Ihnen zwei Registerkarten zur Verfügung. In der ersten können Sie die Grunddaten einer Person bearbeiten. In der zweiten Registerkarte werden die Kontaktdaten der Person hinterlegt. Diese Kontaktdaten sind für die Druckerzeugnisse relevant, da von hier z.B. die postalische Adresse eines Gutachters genommen und automatisch in das Anschreiben übernommen wird.
						</fo:block>
						<fo:block color="#000000" text-align="center" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							<fo:table table-layout="fixed" width="16.5cm">
								<fo:table-column column-width="8cm"/>
								<fo:table-column column-width="0.5cm"/>
								<fo:table-column column-width="8cm"/>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell>
											<fo:block>
												<xsl:call-template name="insertImage">
													<xsl:with-param name="image"><xsl:value-of select="$imagePath" /><xsl:text>/Sachbearbeiter_bearbeiten_01.png</xsl:text></xsl:with-param>
													<xsl:with-param name="width">78mm</xsl:with-param>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell><fo:block /></fo:table-cell>
										<fo:table-cell>
											<fo:block>
												<xsl:call-template name="insertImage">
													<xsl:with-param name="image"><xsl:value-of select="$imagePath" /><xsl:text>/Sachbearbeiter_bearbeiten_02.png</xsl:text></xsl:with-param>
													<xsl:with-param name="width">78mm</xsl:with-param>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell>
											<fo:block>
												<xsl:call-template name="insertImage">
													<xsl:with-param name="image"><xsl:value-of select="$imagePath" /><xsl:text>/Sachbearbeiter_bearbeiten_03.png</xsl:text></xsl:with-param>
													<xsl:with-param name="width">78mm</xsl:with-param>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell><fo:block /></fo:table-cell>
										<fo:table-cell><fo:block /></fo:table-cell>
									</fo:table-row>
								</fo:table-body>
							</fo:table>
						</fo:block>
						<fo:block color="#000000" text-align="justify" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							Wenn Sie sich als Sachbearbeiter/in selbst aufrufen, haben Sie eine dritte Registerkarte, mit welcher Sie Systemeinstellungen für Ihre Einrichtung vornehmen können. ACHTUNG: Diese Daten haben alle Sachbearbeiter/innen Ihrer Einrichtung zur Bearbeitung zur Verfügung. Wenn Sie hier etwas ändern, ändern Sie dies auch für Ihre Kollegen/Kolleginnen.
						</fo:block>
						<fo:block color="#000000" text-align="justify" font-family="{$font}" hyphenate="true" language="de" padding-bottom="1em">
							<fo:table table-layout="fixed" width="16.5cm">
								<fo:table-column column-width="8cm"/>
								<fo:table-column column-width="0.5cm"/>
								<fo:table-column column-width="8cm"/>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell>
											<fo:block>
												<xsl:call-template name="insertImage">
													<xsl:with-param name="image"><xsl:value-of select="$imagePath" /><xsl:text>/Sachbearbeiter_bearbeiten_04.png</xsl:text></xsl:with-param>
													<xsl:with-param name="width">78mm</xsl:with-param>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell><fo:block /></fo:table-cell>
										<fo:table-cell>
											<fo:block>Wenn Sie nun auf den Link „Weitere Einstellungen“ klicken, öffnet sich ein Unterfomular wiederum mit Registerkarten.</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell>
											<fo:block>
												<xsl:call-template name="insertImage">
													<xsl:with-param name="image"><xsl:value-of select="$imagePath" /><xsl:text>/Sachbearbeiter_bearbeiten_05.png</xsl:text></xsl:with-param>
													<xsl:with-param name="width">78mm</xsl:with-param>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell><fo:block /></fo:table-cell>
										<fo:table-cell>
											<fo:block>Hier können Sie die Email-Adresse angeben, an welche z.B. die Informations-Email gesendet werden soll, wenn die Bibliothek ein Abgabedatum eingibt.</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell>
											<fo:block>
												<xsl:call-template name="insertImage">
													<xsl:with-param name="image"><xsl:value-of select="$imagePath" /><xsl:text>/Sachbearbeiter_bearbeiten_06.png</xsl:text></xsl:with-param>
													<xsl:with-param name="width">78mm</xsl:with-param>
												</xsl:call-template>
											</fo:block>
										</fo:table-cell>
										<fo:table-cell><fo:block /></fo:table-cell>
										<fo:table-cell>
											<fo:block>Des Weiteren können Sie hier einen eigenen Text für die Gutachtermahnung dauerhaft hinterlegen, welcher dann in den Druckerzeugnissen für Ihre Einrichtung verwendet wird.</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block-container>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	<xsl:template name="insertImage">
		<xsl:param name="image" />
		<xsl:param name="width" select="null" />
		<xsl:param name="height" select="null" />
		<xsl:variable name="smallShadow">3pt</xsl:variable>
		<xsl:variable name="bigShadow">3pt</xsl:variable>
		<xsl:variable name="type">outset</xsl:variable>
		<xsl:variable name="color">gray</xsl:variable>
		<xsl:variable name="padding">3pt</xsl:variable>
		<xsl:choose>
			<xsl:when test="$width != 'null' and $height != 'null'" >
				<fo:external-graphic src="{$image}" content-height="{$height}" content-width="{$width}" padding="{$padding}" border-left="{$type} {$color} {$smallShadow}" border-top="{$type} {$color} {$smallShadow}" border-bottom="{$type} {$color} {$bigShadow}" border-right="{$type} {$color} {$bigShadow}" />
			</xsl:when>
			<xsl:when test="$width != 'null'" >
				<fo:external-graphic src="{$image}" content-width="{$width}" padding="{$padding}" border-left="{$type} {$color} {$smallShadow}" border-top="{$type} {$color} {$smallShadow}" border-bottom="{$type} {$color} {$bigShadow}" border-right="{$type} {$color} {$bigShadow}" />
			</xsl:when>
			<xsl:when test="$height != 'null'" >
				<fo:external-graphic src="{$image}" content-height="{$height}" padding="{$padding}" border-left="{$type} {$color} {$smallShadow}" border-top="{$type} {$color} {$smallShadow}" border-bottom="{$type} {$color} {$bigShadow}" border-right="{$type} {$color} {$bigShadow}" />
			</xsl:when>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
