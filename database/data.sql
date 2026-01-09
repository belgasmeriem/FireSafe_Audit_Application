--
-- PostgreSQL database dump
--

\restrict ectBapDlz4f3UaN99JzG6BWrQWQMV6Db6S3xBSjD0Crpv7zDNjztdrDJh2HjzYi

-- Dumped from database version 18.1
-- Dumped by pg_dump version 18.1

-- Started on 2026-01-09 21:35:56

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 5186 (class 0 OID 16390)
-- Dependencies: 220
-- Data for Name: audits; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.audits (id, date_audit, date_cloture, date_creation, date_debut, date_modification, date_validation, duree_estimee, duree_reelle, nb_conformes, nb_non_applicables, nb_non_conformes, nb_partiels, observation_generale, statut, taux_conformite, taux_conformite_pondere, type, audit_initial_id, auditeur_id, etablissement_id, validateur_id) FROM stdin;
1	2026-01-15	2026-01-09 19:40:20.933989	2026-01-09 19:34:45.880407	2026-01-09 19:38:34.046216	2026-01-09 19:47:38.369668	2026-01-09 19:47:38.318786	6	1	0	0	0	0		VALIDE	90.97	\N	INITIAL	\N	3	1	2
\.


--
-- TOC entry 5188 (class 0 OID 16406)
-- Dependencies: 222
-- Data for Name: commentaires; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.commentaires (id, contenu, date_creation, date_modification, audit_id, auteur_id, parent_id) FROM stdin;
1	Excellent travail ! L'audit révèle un taux de conformité très satisfaisant de 91%. \r\nL'établissement démontre un engagement sérieux envers la sécurité incendie.	2026-01-09 19:50:52.529687	2026-01-09 19:50:52.529687	1	2	\N
\.


--
-- TOC entry 5190 (class 0 OID 16419)
-- Dependencies: 224
-- Data for Name: criteres; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.criteres (id, categorie, code, criticite, description, libelle, obligatoire, ponderation, preuves_requises, reference_texte_loi, norme_id, section_id) FROM stdin;
149	ACCESSIBILITE	U-ACC-01	CRITIQUE	\N	Façade accessible avec voies échelles selon effectif	t	5	\N	2.5.1.3	3	31
150	ACCESSIBILITE	U-ACC-02	CRITIQUE	\N	Accès supplémentaire pour services de secours à tous les étages	t	5	\N	2.6.10.5	3	31
151	ACCESSIBILITE	U-ACC-03	IMPORTANTE	\N	Voies engins conformes au règlement	t	4	\N	2.5.1.1	3	31
152	ISOLEMENT_TIERS	U-ISO-01	CRITIQUE	\N	Isolement par rapport aux tiers - Distance 8m ou murs CF	t	5	\N	2.5.2	3	32
153	ISOLEMENT_TIERS	U-ISO-02	CRITIQUE	\N	Isolement des niveaux par planchers CF	t	5	\N	2.5.2	3	32
154	RESISTANCE_FEU_STRUCTURES	U-STR-01	CRITIQUE	\N	Structure SF 1h (si plancher > 8m)	t	5	\N	2.5.3	3	33
155	RESISTANCE_FEU_STRUCTURES	U-STR-02	CRITIQUE	\N	Planchers CF 1h (si plancher > 8m)	t	5	\N	2.5.3	3	33
156	FACADE	U-FAC-01	IMPORTANTE	\N	Revêtements extérieurs de façade M3	t	4	\N	2.5.4	3	34
157	FACADE	U-FAC-02	IMPORTANTE	\N	Règle C+D respectée selon masse combustible	t	4	\N	2.5.4	3	34
158	FACADE	U-FAC-03	NORMALE	\N	Menuiseries et garde-corps M3	t	3	\N	2.5.4	3	34
159	DISTRIBUTION_INTERIEURE	U-DIS-01	CRITIQUE	\N	Parois verticales CF 1h entre locaux et dégagements	t	5	\N	2.5.5.1	3	35
160	DISTRIBUTION_INTERIEURE	U-DIS-02	CRITIQUE	\N	Blocs-portes PF 1/2h	t	5	\N	2.5.5.1	3	35
161	DISTRIBUTION_INTERIEURE	U-DIS-03	IMPORTANTE	\N	Recoupement circulations horizontales tous les 25-30m par portes PF 1/2h	t	4	\N	2.5.5.1	3	35
162	DISTRIBUTION_INTERIEURE	U-ZON-01	CRITIQUE	\N	Niveaux avec locaux à sommeil divisés en zones protégées	t	5	\N	2.6.10.5	3	35
163	DISTRIBUTION_INTERIEURE	U-ZON-02	CRITIQUE	\N	Cloison CF 1h entre zones protégées de façade à façade	t	5	\N	2.6.10.5	3	35
164	DISTRIBUTION_INTERIEURE	U-ZON-03	IMPORTANTE	\N	Zones protégées de capacité d'accueil similaire	t	4	\N	2.6.10.5	3	35
165	DISTRIBUTION_INTERIEURE	U-ZON-04	IMPORTANTE	\N	Passage entre zones protégées uniquement par circulations	t	4	\N	2.6.10.5	3	35
166	LOCAUX_RISQUE	U-LRI-01	CRITIQUE	\N	Chaufferie > 70kW - Planchers et parois CF 2h	t	5	\N	2.5.6.1	3	36
167	LOCAUX_RISQUE	U-LRI-02	CRITIQUE	\N	Chaufferie > 70kW - Portes CF 1h vers sortie avec ferme-porte	t	5	\N	2.5.6.1	3	36
168	LOCAUX_RISQUE	U-LRI-03	CRITIQUE	\N	Chaufferie > 70kW - Pas de communication directe avec public	t	5	\N	2.5.6.1	3	36
169	LOCAUX_RISQUE	U-LRI-04	CRITIQUE	\N	Locaux groupes électrogènes - CF 2h	t	5	\N	2.5.6.1	3	36
170	LOCAUX_RISQUE	U-LRI-05	CRITIQUE	\N	Postes transformation électrique - CF 2h	t	5	\N	2.5.6.1	3	36
171	LOCAUX_RISQUE	U-LRI-06	CRITIQUE	\N	Locaux vide-ordures - CF 2h	t	5	\N	2.5.6.1	3	36
172	LOCAUX_RISQUE	U-LRM-01	IMPORTANTE	\N	Chaufferie 30-70kW - Planchers et parois CF 1h	t	4	\N	2.5.6.1	3	36
173	LOCAUX_RISQUE	U-LRM-02	IMPORTANTE	\N	Chaufferie 30-70kW - Portes CF 1/2h avec ferme-porte	t	4	\N	2.5.6.1	3	36
174	LOCAUX_RISQUE	U-LRM-03	IMPORTANTE	\N	Machineries ascenseurs - Isolement CF 1h	t	4	\N	2.5.6.1	3	36
175	LOCAUX_RISQUE	U-LRM-04	IMPORTANTE	\N	Locaux VMC - Isolement CF 1h	t	4	\N	2.5.6.1	3	36
176	LOCAUX_RISQUE	U-LRM-05	IMPORTANTE	\N	Cuisines collectives > 20kW - Isolement CF 1h	t	4	\N	2.5.6.1	3	36
177	LOCAUX_RISQUE	U-LRM-06	IMPORTANTE	\N	Lingeries et blanchisseries - Isolement CF 1h	t	4	\N	2.5.6.1	3	36
178	LOCAUX_RISQUE	U-LRM-07	IMPORTANTE	\N	Bagageries - Isolement CF 1h	t	4	\N	2.5.6.1	3	36
179	LOCAUX_RISQUE	U-LRM-08	IMPORTANTE	\N	Ateliers maintenance - Isolement CF 1h	t	4	\N	2.5.6.1	3	36
180	LOCAUX_RISQUE	U-LRM-09	IMPORTANTE	\N	Dépôts produits inflammables - Isolement CF 1h	t	4	\N	2.5.6.1	3	36
181	LOCAUX_RISQUE	U-LOG-01	IMPORTANTE	\N	Logements personnel - Isolement selon locaux sommeil	t	4	\N	2.5.6.2	3	36
182	CONDUITS_GAINES	U-CON-01	IMPORTANTE	\N	Conduits Ø > 75mm - Résistance au feu conforme	t	4	\N	2.5.7	3	37
183	CONDUITS_GAINES	U-CON-02	IMPORTANTE	\N	Conduits aérauliques en matériaux M0	t	4	\N	2.5.7	3	37
184	CONDUITS_GAINES	U-CON-03	IMPORTANTE	\N	Gaines techniques CF de traversée = CF plancher (max 1h)	t	4	\N	2.5.7.2	3	37
185	CONDUITS_GAINES	U-CON-04	IMPORTANTE	\N	Gaines verticales recoupées tous les 2 niveaux	t	4	\N	2.5.7.2	3	37
186	CONDUITS_GAINES	U-CON-05	NORMALE	\N	Trappes de visite des gaines PF 1/2h	t	3	\N	2.5.7.2	3	37
187	DEGAGEMENTS	U-DEG-01	CRITIQUE	\N	Circulations horizontales largeur minimale 2 UP (1,40m)	t	5	\N	2.6.10.5	3	38
188	DEGAGEMENTS	U-DEG-02	CRITIQUE	\N	Distance maximale 40m vers escalier	t	5	\N	2.6.10.5	3	38
189	DEGAGEMENTS	U-DEG-03	CRITIQUE	\N	Distance maximale 30m en cul-de-sac	t	5	\N	2.6.10.5	3	38
190	DEGAGEMENTS	U-DEG-04	CRITIQUE	\N	Portes locaux > 50 personnes s'ouvrent vers sortie	t	5	\N	2.5.8.4	3	38
191	DEGAGEMENTS	U-DEG-05	CRITIQUE	\N	Ouverture portes par simple poussée ou dispositif facile	t	5	\N	2.5.8.4	3	38
192	DEGAGEMENTS	U-DEG-06	CRITIQUE	\N	Portes recoupement en va-et-vient	t	5	\N	2.6.10.5	3	38
193	DEGAGEMENTS	U-DEG-07	CRITIQUE	\N	Calcul nombre dégagements selon effectif réglementaire	t	5	\N	2.5.8.6	3	38
194	DEGAGEMENTS	U-DEG-08	CRITIQUE	\N	Largeur dégagements conforme au tableau réglementaire	t	5	\N	2.5.8.6	3	38
195	DEGAGEMENTS	U-DEG-09	IMPORTANTE	\N	Absence de marches isolées dans circulations principales	t	4	\N	2.5.8.1	3	38
196	DEGAGEMENTS	U-DEG-10	NORMALE	\N	Portes cul-de-sac signalées "Sans issue"	t	3	\N	2.5.8.4	3	38
197	ESCALIERS	U-ESC-01	CRITIQUE	\N	Tous les escaliers sont protégés (encloisonnés)	t	5	\N	2.6.10.5	3	39
198	ESCALIERS	U-ESC-02	CRITIQUE	\N	Escaliers desservant malades non-autonomes - Largeur 2 UP minimum	t	5	\N	2.6.10.5	3	39
199	ESCALIERS	U-ESC-03	CRITIQUE	\N	Escaliers continus jusqu'au niveau d'évacuation	t	5	\N	2.5.8.5	3	39
200	ESCALIERS	U-ESC-04	CRITIQUE	\N	Séparation escaliers étages/sous-sols contre fumées	t	5	\N	2.5.8.5	3	39
201	ESCALIERS	U-ESC-05	CRITIQUE	\N	Portes escaliers PF 1/2h avec ferme-porte	t	5	\N	2.5.8.5	3	39
202	ESCALIERS	U-ESC-06	IMPORTANTE	\N	Répartition escaliers - Distance minimale 5m	t	4	\N	2.5.8.5	3	39
203	ESCALIERS	U-ESC-07	NORMALE	\N	Porte accès escalier 2 UP peut être de 1 UP	t	3	\N	2.6.10.5	3	39
204	AMENAGEMENTS_INTERIEURS	U-AME-01	NORMALE	\N	Gros mobilier et rayonnages en M3	t	3	\N	2.5.9	3	40
205	AMENAGEMENTS_INTERIEURS	U-AME-02	NORMALE	\N	Cloisons mobiles en M3	t	3	\N	2.5.9	3	40
206	AMENAGEMENTS_INTERIEURS	U-REV-01	IMPORTANTE	\N	Revêtements locaux - Plafonds M1	t	4	\N	2.5.9.1	3	40
207	AMENAGEMENTS_INTERIEURS	U-REV-02	IMPORTANTE	\N	Revêtements locaux - Parois M2	t	4	\N	2.5.9.1	3	40
208	AMENAGEMENTS_INTERIEURS	U-REV-03	NORMALE	\N	Revêtements locaux - Sols M4	t	3	\N	2.5.9.1	3	40
209	AMENAGEMENTS_INTERIEURS	U-REV-04	IMPORTANTE	\N	Revêtements circulations - Plafonds M1	t	4	\N	2.5.9.2	3	40
210	AMENAGEMENTS_INTERIEURS	U-REV-05	IMPORTANTE	\N	Revêtements circulations - Cloisons M2	t	4	\N	2.5.9.2	3	40
211	AMENAGEMENTS_INTERIEURS	U-REV-06	NORMALE	\N	Revêtements circulations - Sols M4	t	3	\N	2.5.9.2	3	40
212	AMENAGEMENTS_INTERIEURS	U-REV-07	IMPORTANTE	\N	Revêtements escaliers - Plafonds et murs M1	t	4	\N	2.5.9.3	3	40
213	AMENAGEMENTS_INTERIEURS	U-REV-08	NORMALE	\N	Revêtements escaliers - Marches M3	t	3	\N	2.5.9.3	3	40
214	DESENFUMAGE	U-DES-01	CRITIQUE	\N	Désenfumage mécanique des circulations horizontales avec locaux sommeil	t	5	\N	2.6.10.3	3	41
215	DESENFUMAGE	U-DES-02	CRITIQUE	\N	Désenfumage circulations quelle que soit longueur (avec sommeil)	t	5	\N	2.6.10.3	3	41
216	DESENFUMAGE	U-DES-03	CRITIQUE	\N	Désenfumage asservi à détection automatique zone sinistrée	t	5	\N	2.6.10.3	3	41
217	DESENFUMAGE	U-DES-04	CRITIQUE	\N	Halls évacuation public désenfumés	t	5	\N	2.6.10.3	3	41
218	DESENFUMAGE	U-DES-05	IMPORTANTE	\N	Désenfumage naturel autorisé si R+1 maximum	t	4	\N	2.6.10.3	3	41
219	DESENFUMAGE	U-DES-06	IMPORTANTE	\N	Ventilateurs réalimentés par groupe électrogène si existant	t	4	\N	2.6.10.3	3	41
220	DESENFUMAGE	U-DES-07	IMPORTANTE	\N	Circulations menant aux blocs opératoires désenfumées	t	4	\N	2.6.10.4	3	41
221	DESENFUMAGE	U-DES-08	IMPORTANTE	\N	Locaux > 100m² en sous-sol désenfumés	t	4	\N	2.5.10.4	3	41
222	DESENFUMAGE	U-DES-09	IMPORTANTE	\N	Locaux > 300m² en RDC et étage désenfumés	t	4	\N	2.5.10.4	3	41
223	DESENFUMAGE	U-DES-10	IMPORTANTE	\N	Locaux > 100m² sans ouverture extérieure désenfumés	t	4	\N	2.5.10.4	3	41
224	DESENFUMAGE	U-DES-11	IMPORTANTE	\N	Commande désenfumage avant extinction automatique	t	4	\N	2.5.10.3	3	41
225	DESENFUMAGE	U-DES-12	NORMALE	\N	Ventilation confort arrêtée lors désenfumage	t	3	\N	2.5.10.3	3	41
226	DESENFUMAGE	U-DES-13	IMPORTANTE	\N	Exutoires et dispositifs conformes aux normes	t	4	\N	2.5.10.2	3	41
227	DESENFUMAGE	U-DES-14	IMPORTANTE	\N	Entretien et vérifications périodiques désenfumage	t	4	\N	2.5.10.5	3	41
228	CHAUFFAGE	U-CHA-01	IMPORTANTE	\N	Systèmes chauffage centralisé conformes	t	4	\N	2.5.11	3	42
229	CHAUFFAGE	U-CHA-02	IMPORTANTE	\N	Restrictions appareils indépendants respectées	t	4	\N	2.5.11	3	42
230	INSTALLATIONS_ELECTRIQUES	U-ELE-01	CRITIQUE	\N	Installations électriques conformes aux normes	t	5	\N	2.5.12.2	3	43
231	INSTALLATIONS_ELECTRIQUES	U-ELE-02	IMPORTANTE	\N	Pas de canalisations étrangères sauf cheminements protégés CF 1h	t	4	\N	2.5.12.2	3	43
232	INSTALLATIONS_ELECTRIQUES	U-ELE-03	IMPORTANTE	\N	Installations locaux public/non-public commandées indépendamment	t	4	\N	2.5.12.2	3	43
233	INSTALLATIONS_ELECTRIQUES	U-ELE-04	CRITIQUE	\N	Source remplacement alimente éclairage sécurité	t	5	\N	2.5.12.2	3	43
234	INSTALLATIONS_ELECTRIQUES	U-ELE-05	IMPORTANTE	\N	Tension max basse tension dans locaux public	t	4	\N	2.5.12.2	3	43
235	ECLAIRAGE_SECURITE	U-ECL-01	CRITIQUE	\N	Éclairage évacuation dans locaux ≥ 50 personnes	t	5	\N	2.5.13	3	44
236	ECLAIRAGE_SECURITE	U-ECL-02	CRITIQUE	\N	Éclairage évacuation dans locaux > 300m² (étage/RDC)	t	5	\N	2.5.13	3	44
237	ECLAIRAGE_SECURITE	U-ECL-03	CRITIQUE	\N	Éclairage évacuation dans locaux > 100m² (sous-sol)	t	5	\N	2.5.13	3	44
238	ECLAIRAGE_SECURITE	U-ECL-04	CRITIQUE	\N	Éclairage ambiance si > 100 pers. (étage/RDC)	t	5	\N	2.5.13	3	44
239	ECLAIRAGE_SECURITE	U-ECL-05	CRITIQUE	\N	Éclairage ambiance si > 50 pers. (sous-sol)	t	5	\N	2.5.13	3	44
240	ECLAIRAGE_SECURITE	U-ECL-06	IMPORTANTE	\N	Flux lumineux ≥ 5 lm/m² pour éclairage ambiance	t	4	\N	2.5.13	3	44
241	ECLAIRAGE_SECURITE	U-ECL-07	IMPORTANTE	\N	Éclairage sécurité par source centralisée ou blocs autonomes	t	4	\N	2.5.13	3	44
242	COLONNES_SECHES	U-COL-01	CRITIQUE	\N	Colonnes sèches si plancher > 18m	t	5	\N	2.5.14.1	3	45
243	COLONNES_SECHES	U-COL-02	CRITIQUE	\N	Colonne sèche dans escaliers sous-sols > 1 niveau	t	5	\N	2.6.10.6	3	45
244	COLONNES_SECHES	U-COL-03	CRITIQUE	\N	Colonnes sèches conformes au Livre 1	t	5	\N	2.5.14.1	3	45
245	ROBINETS_INCENDIE_ARMEE	U-RIA-01	CRITIQUE	\N	RIA dans établissements 1ère catégorie	t	5	\N	2.6.10.6	3	46
246	ROBINETS_INCENDIE_ARMEE	U-RIA-02	IMPORTANTE	\N	RIA dans zones accès difficile	t	4	\N	2.6.10.6	3	46
247	ROBINETS_INCENDIE_ARMEE	U-RIA-03	IMPORTANTE	\N	RIA dans bâtiments distribution compliquée	t	4	\N	2.6.10.6	3	46
248	EXTINCTEURS	U-EXT-01	IMPORTANTE	\N	Extincteurs portables répartis judicieusement	t	4	\N	Livre 1	3	47
249	EXTINCTEURS	U-EXT-02	IMPORTANTE	\N	Extincteurs appropriés aux risques	t	4	\N	Livre 1	3	47
250	EXTINCTEURS	U-EXT-03	NORMALE	\N	Extincteurs accessibles et signalisés	t	3	\N	Livre 1	3	47
251	SYSTEME_EXTINCTION_AUTOMATIQUE	U-SPK-01	IMPORTANTE	\N	Sprinklers dans locaux haut risque si demandé	t	4	\N	2.6.10.6	3	48
252	SYSTEME_EXTINCTION_AUTOMATIQUE	U-SPK-02	IMPORTANTE	\N	Sprinklers conformes aux normes	t	4	\N	2.5.14.3	3	48
253	SYSTEME_EXTINCTION_AUTOMATIQUE	U-SPK-03	IMPORTANTE	\N	Isolation partie protégée/non protégée par sprinklers	t	4	\N	2.5.14.3	3	48
254	DETECTION_ALARME	U-SSI-01	CRITIQUE	\N	SSI catégorie A obligatoire si locaux sommeil	t	5	\N	2.6.10.6	3	49
255	DETECTION_ALARME	U-SSI-02	CRITIQUE	\N	Détecteurs dans ensemble établissement (sauf escaliers/sanitaires)	t	5	\N	2.6.10.6	3	49
256	DETECTION_ALARME	U-SSI-03	CRITIQUE	\N	Détecteurs appropriés aux risques	t	5	\N	2.6.10.6	3	49
257	DETECTION_ALARME	U-DET-01	IMPORTANTE	\N	Indicateur action détecteurs visible dans circulation (locaux sommeil)	t	4	\N	2.6.10.6	3	49
258	DETECTION_ALARME	U-DET-02	CRITIQUE	\N	Détection locaux déclenche alarme générale sélective	t	5	\N	2.6.10.6	3	49
259	DETECTION_ALARME	U-DET-03	CRITIQUE	\N	Détection locaux déclenche DAS compartimentage zone	t	5	\N	2.6.10.6	3	49
260	DETECTION_ALARME	U-DET-04	CRITIQUE	\N	Détection locaux déclenche déverrouillage portes	t	5	\N	2.6.10.6	3	49
261	DETECTION_ALARME	U-DET-05	IMPORTANTE	\N	Détection locaux - Non-arrêt ascenseurs zone sinistrée	t	4	\N	2.6.10.6	3	49
262	DETECTION_ALARME	U-DET-06	CRITIQUE	\N	Détection locaux déclenche désenfumage local sinistré	t	5	\N	2.6.10.6	3	49
263	DETECTION_ALARME	U-DET-07	CRITIQUE	\N	Détection circulations déclenche alarme générale sélective	t	5	\N	2.6.10.6	3	49
264	DETECTION_ALARME	U-DET-08	CRITIQUE	\N	Détection circulations déclenche DAS compartimentage	t	5	\N	2.6.10.6	3	49
265	DETECTION_ALARME	U-DET-09	CRITIQUE	\N	Détection circulations déclenche désenfumage circulation zone	t	5	\N	2.6.10.6	3	49
266	DETECTION_ALARME	U-DET-10	IMPORTANTE	\N	Détection combles déclenche alarme générale sélective	t	4	\N	2.6.10.6	3	49
267	DETECTION_ALARME	U-UAE-01	IMPORTANTE	\N	UAE avec tableaux report si > 2500 personnes	t	4	\N	2.6.10.6	3	49
268	DETECTION_ALARME	U-UAE-02	IMPORTANTE	\N	UAE alimentée par source sécurité	t	4	\N	2.6.10.6	3	49
269	DETECTION_ALARME	U-CEN-01	NORMALE	\N	Centralisation SSI autorisée si plusieurs bâtiments	t	3	\N	2.6.10.6	3	49
270	DETECTION_ALARME	U-FER-01	CRITIQUE	\N	Fermeture simultanée portes auto asservie à détection	t	5	\N	2.6.10.5	3	49
271	DETECTION_ALARME	U-FER-02	CRITIQUE	\N	Fermeture portes recoupement au niveau sinistré	t	5	\N	2.6.10.5	3	49
272	CONSIGNES_SECURITE	U-VER-01	IMPORTANTE	\N	Services psychiatrie - Verrouillage exceptionnel autorisé	t	4	\N	2.6.10.5	3	50
273	CONSIGNES_SECURITE	U-VER-02	CRITIQUE	\N	Verrouillage - Surveillance permanente par préposé	t	5	\N	2.6.10.5	3	50
274	CONSIGNES_SECURITE	U-VER-03	CRITIQUE	\N	Interdiction clés sous verre dormant ou crémones	t	5	\N	2.6.10.5	3	50
275	CONSIGNES_SECURITE	U-VER-04	CRITIQUE	\N	Personnels dotés clés correspondantes	t	5	\N	2.6.10.5	3	50
276	CONSIGNES_SECURITE	U-VER-05	IMPORTANTE	\N	Maternités - Verrouillage exceptionnel autorisé avec surveillance	t	4	\N	2.6.10.5	3	50
277	CONSIGNES_SECURITE	U-VER-06	IMPORTANTE	\N	Établissements enfants - Verrouillage exceptionnel avec surveillance	t	4	\N	2.6.10.5	3	50
278	MOYENS_SECOURS	U-SEC-01	CRITIQUE	\N	Service sécurité assuré par agents sécurité	t	5	\N	2.6.10.6	3	51
279	MOYENS_SECOURS	U-SEC-02	CRITIQUE	\N	Surveillance permanente des bâtiments	t	5	\N	2.6.10.6	3	51
280	FORMATION_PERSONNEL	U-FOR-01	IMPORTANTE	\N	Personnel formé aux consignes sécurité	t	4	\N	Dispositions générales	3	52
281	EXERCICES_EVACUATION	U-FOR-02	IMPORTANTE	\N	Exercices évacuation réguliers	t	4	\N	Dispositions générales	3	53
282	FORMATION_PERSONNEL	U-FOR-03	IMPORTANTE	\N	Personnel formé à l'utilisation des extincteurs	t	4	\N	Dispositions générales	3	52
283	CONSIGNES_SECURITE	U-CON-06	IMPORTANTE	\N	Consignes sécurité affichées et actualisées	t	4	\N	Dispositions générales	3	50
284	PLANS_EVACUATION	U-CON-07	IMPORTANTE	\N	Plans évacuation affichés à chaque niveau	t	4	\N	Dispositions générales	3	54
285	REGISTRE_SECURITE	U-REG-01	IMPORTANTE	\N	Registre sécurité tenu à jour	t	4	\N	Dispositions générales	3	55
286	REGISTRE_SECURITE	U-REG-02	IMPORTANTE	\N	Registre contient résultats vérifications périodiques	t	4	\N	Dispositions générales	3	55
287	VERIFICATION_PERIODIQUE	U-VER-07	IMPORTANTE	\N	Vérifications périodiques installations sécurité	t	4	\N	Dispositions générales	3	56
288	VERIFICATION_PERIODIQUE	U-VER-08	IMPORTANTE	\N	Vérifications par organismes agréés	t	4	\N	Dispositions générales	3	56
289	MAINTENANCE_EQUIPEMENTS	U-MAI-01	IMPORTANTE	\N	Contrat maintenance installations sécurité	t	4	\N	Dispositions générales	3	57
290	MAINTENANCE_EQUIPEMENTS	U-MAI-02	IMPORTANTE	\N	Maintenance désenfumage	t	4	\N	2.5.10.5	3	57
291	MAINTENANCE_EQUIPEMENTS	U-MAI-03	IMPORTANTE	\N	Maintenance détection incendie	t	4	\N	Dispositions générales	3	57
292	MAINTENANCE_EQUIPEMENTS	U-MAI-04	IMPORTANTE	\N	Maintenance éclairage sécurité	t	4	\N	Dispositions générales	3	57
293	CONTROLES_REGLEMENTAIRES	U-CTR-01	IMPORTANTE	\N	Contrôles réglementaires annuels	t	4	\N	Dispositions générales	3	58
294	CONTROLES_REGLEMENTAIRES	U-CTR-02	IMPORTANTE	\N	Contrôles par commission sécurité	t	4	\N	Dispositions générales	3	58
295	ACCESSIBILITE	U-ACC-01	CRITIQUE	\N	Façade accessible avec voies échelles selon effectif	t	5	\N	2.5.1.3	4	59
296	ACCESSIBILITE	U-ACC-02	CRITIQUE	\N	Accès supplémentaire pour services de secours à tous les étages	t	5	\N	2.6.10.5	4	59
297	ACCESSIBILITE	U-ACC-03	IMPORTANTE	\N	Voies engins conformes au règlement	t	4	\N	2.5.1.1	4	59
298	ISOLEMENT_TIERS	U-ISO-01	CRITIQUE	\N	Isolement par rapport aux tiers - Distance 8m ou murs CF	t	5	\N	2.5.2	4	60
299	ISOLEMENT_TIERS	U-ISO-02	CRITIQUE	\N	Isolement des niveaux par planchers CF	t	5	\N	2.5.2	4	60
300	RESISTANCE_FEU_STRUCTURES	U-STR-01	CRITIQUE	\N	Structure SF 1h (si plancher > 8m)	t	5	\N	2.5.3	4	61
301	RESISTANCE_FEU_STRUCTURES	U-STR-02	CRITIQUE	\N	Planchers CF 1h (si plancher > 8m)	t	5	\N	2.5.3	4	61
302	FACADE	U-FAC-01	IMPORTANTE	\N	Revêtements extérieurs de façade M3	t	4	\N	2.5.4	4	62
303	FACADE	U-FAC-02	IMPORTANTE	\N	Règle C+D respectée selon masse combustible	t	4	\N	2.5.4	4	62
304	FACADE	U-FAC-03	NORMALE	\N	Menuiseries et garde-corps M3	t	3	\N	2.5.4	4	62
305	DISTRIBUTION_INTERIEURE	U-DIS-01	CRITIQUE	\N	Parois verticales CF 1h entre locaux et dégagements	t	5	\N	2.5.5.1	4	63
306	DISTRIBUTION_INTERIEURE	U-DIS-02	CRITIQUE	\N	Blocs-portes PF 1/2h	t	5	\N	2.5.5.1	4	63
307	DISTRIBUTION_INTERIEURE	U-DIS-03	IMPORTANTE	\N	Recoupement circulations horizontales tous les 25-30m par portes PF 1/2h	t	4	\N	2.5.5.1	4	63
308	DISTRIBUTION_INTERIEURE	U-ZON-01	CRITIQUE	\N	Niveaux avec locaux à sommeil divisés en zones protégées	t	5	\N	2.6.10.5	4	63
309	DISTRIBUTION_INTERIEURE	U-ZON-02	CRITIQUE	\N	Cloison CF 1h entre zones protégées de façade à façade	t	5	\N	2.6.10.5	4	63
310	DISTRIBUTION_INTERIEURE	U-ZON-03	IMPORTANTE	\N	Zones protégées de capacité d'accueil similaire	t	4	\N	2.6.10.5	4	63
311	DISTRIBUTION_INTERIEURE	U-ZON-04	IMPORTANTE	\N	Passage entre zones protégées uniquement par circulations	t	4	\N	2.6.10.5	4	63
312	LOCAUX_RISQUE	U-LRI-01	CRITIQUE	\N	Chaufferie > 70kW - Planchers et parois CF 2h	t	5	\N	2.5.6.1	4	64
313	LOCAUX_RISQUE	U-LRI-02	CRITIQUE	\N	Chaufferie > 70kW - Portes CF 1h vers sortie avec ferme-porte	t	5	\N	2.5.6.1	4	64
314	LOCAUX_RISQUE	U-LRI-03	CRITIQUE	\N	Chaufferie > 70kW - Pas de communication directe avec public	t	5	\N	2.5.6.1	4	64
315	LOCAUX_RISQUE	U-LRI-04	CRITIQUE	\N	Locaux groupes électrogènes - CF 2h	t	5	\N	2.5.6.1	4	64
316	LOCAUX_RISQUE	U-LRI-05	CRITIQUE	\N	Postes transformation électrique - CF 2h	t	5	\N	2.5.6.1	4	64
317	LOCAUX_RISQUE	U-LRI-06	CRITIQUE	\N	Locaux vide-ordures - CF 2h	t	5	\N	2.5.6.1	4	64
318	LOCAUX_RISQUE	U-LRM-01	IMPORTANTE	\N	Chaufferie 30-70kW - Planchers et parois CF 1h	t	4	\N	2.5.6.1	4	64
319	LOCAUX_RISQUE	U-LRM-02	IMPORTANTE	\N	Chaufferie 30-70kW - Portes CF 1/2h avec ferme-porte	t	4	\N	2.5.6.1	4	64
320	LOCAUX_RISQUE	U-LRM-03	IMPORTANTE	\N	Machineries ascenseurs - Isolement CF 1h	t	4	\N	2.5.6.1	4	64
321	LOCAUX_RISQUE	U-LRM-04	IMPORTANTE	\N	Locaux VMC - Isolement CF 1h	t	4	\N	2.5.6.1	4	64
322	LOCAUX_RISQUE	U-LRM-05	IMPORTANTE	\N	Cuisines collectives > 20kW - Isolement CF 1h	t	4	\N	2.5.6.1	4	64
323	LOCAUX_RISQUE	U-LRM-06	IMPORTANTE	\N	Lingeries et blanchisseries - Isolement CF 1h	t	4	\N	2.5.6.1	4	64
324	LOCAUX_RISQUE	U-LRM-07	IMPORTANTE	\N	Bagageries - Isolement CF 1h	t	4	\N	2.5.6.1	4	64
325	LOCAUX_RISQUE	U-LRM-08	IMPORTANTE	\N	Ateliers maintenance - Isolement CF 1h	t	4	\N	2.5.6.1	4	64
326	LOCAUX_RISQUE	U-LRM-09	IMPORTANTE	\N	Dépôts produits inflammables - Isolement CF 1h	t	4	\N	2.5.6.1	4	64
327	LOCAUX_RISQUE	U-LOG-01	IMPORTANTE	\N	Logements personnel - Isolement selon locaux sommeil	t	4	\N	2.5.6.2	4	64
328	CONDUITS_GAINES	U-CON-01	IMPORTANTE	\N	Conduits Ø > 75mm - Résistance au feu conforme	t	4	\N	2.5.7	4	65
329	CONDUITS_GAINES	U-CON-02	IMPORTANTE	\N	Conduits aérauliques en matériaux M0	t	4	\N	2.5.7	4	65
330	CONDUITS_GAINES	U-CON-03	IMPORTANTE	\N	Gaines techniques CF de traversée = CF plancher (max 1h)	t	4	\N	2.5.7.2	4	65
331	CONDUITS_GAINES	U-CON-04	IMPORTANTE	\N	Gaines verticales recoupées tous les 2 niveaux	t	4	\N	2.5.7.2	4	65
332	CONDUITS_GAINES	U-CON-05	NORMALE	\N	Trappes de visite des gaines PF 1/2h	t	3	\N	2.5.7.2	4	65
333	DEGAGEMENTS	U-DEG-01	CRITIQUE	\N	Circulations horizontales largeur minimale 2 UP (1,40m)	t	5	\N	2.6.10.5	4	66
334	DEGAGEMENTS	U-DEG-02	CRITIQUE	\N	Distance maximale 40m vers escalier	t	5	\N	2.6.10.5	4	66
335	DEGAGEMENTS	U-DEG-03	CRITIQUE	\N	Distance maximale 30m en cul-de-sac	t	5	\N	2.6.10.5	4	66
336	DEGAGEMENTS	U-DEG-04	CRITIQUE	\N	Portes locaux > 50 personnes s'ouvrent vers sortie	t	5	\N	2.5.8.4	4	66
337	DEGAGEMENTS	U-DEG-05	CRITIQUE	\N	Ouverture portes par simple poussée ou dispositif facile	t	5	\N	2.5.8.4	4	66
338	DEGAGEMENTS	U-DEG-06	CRITIQUE	\N	Portes recoupement en va-et-vient	t	5	\N	2.6.10.5	4	66
339	DEGAGEMENTS	U-DEG-07	CRITIQUE	\N	Calcul nombre dégagements selon effectif réglementaire	t	5	\N	2.5.8.6	4	66
340	DEGAGEMENTS	U-DEG-08	CRITIQUE	\N	Largeur dégagements conforme au tableau réglementaire	t	5	\N	2.5.8.6	4	66
341	DEGAGEMENTS	U-DEG-09	IMPORTANTE	\N	Absence de marches isolées dans circulations principales	t	4	\N	2.5.8.1	4	66
342	DEGAGEMENTS	U-DEG-10	NORMALE	\N	Portes cul-de-sac signalées "Sans issue"	t	3	\N	2.5.8.4	4	66
343	ESCALIERS	U-ESC-01	CRITIQUE	\N	Tous les escaliers sont protégés (encloisonnés)	t	5	\N	2.6.10.5	4	67
344	ESCALIERS	U-ESC-02	CRITIQUE	\N	Escaliers desservant malades non-autonomes - Largeur 2 UP minimum	t	5	\N	2.6.10.5	4	67
345	ESCALIERS	U-ESC-03	CRITIQUE	\N	Escaliers continus jusqu'au niveau d'évacuation	t	5	\N	2.5.8.5	4	67
346	ESCALIERS	U-ESC-04	CRITIQUE	\N	Séparation escaliers étages/sous-sols contre fumées	t	5	\N	2.5.8.5	4	67
347	ESCALIERS	U-ESC-05	CRITIQUE	\N	Portes escaliers PF 1/2h avec ferme-porte	t	5	\N	2.5.8.5	4	67
348	ESCALIERS	U-ESC-06	IMPORTANTE	\N	Répartition escaliers - Distance minimale 5m	t	4	\N	2.5.8.5	4	67
349	ESCALIERS	U-ESC-07	NORMALE	\N	Porte accès escalier 2 UP peut être de 1 UP	t	3	\N	2.6.10.5	4	67
350	AMENAGEMENTS_INTERIEURS	U-AME-01	NORMALE	\N	Gros mobilier et rayonnages en M3	t	3	\N	2.5.9	4	68
351	AMENAGEMENTS_INTERIEURS	U-AME-02	NORMALE	\N	Cloisons mobiles en M3	t	3	\N	2.5.9	4	68
352	AMENAGEMENTS_INTERIEURS	U-REV-01	IMPORTANTE	\N	Revêtements locaux - Plafonds M1	t	4	\N	2.5.9.1	4	68
353	AMENAGEMENTS_INTERIEURS	U-REV-02	IMPORTANTE	\N	Revêtements locaux - Parois M2	t	4	\N	2.5.9.1	4	68
354	AMENAGEMENTS_INTERIEURS	U-REV-03	NORMALE	\N	Revêtements locaux - Sols M4	t	3	\N	2.5.9.1	4	68
355	AMENAGEMENTS_INTERIEURS	U-REV-04	IMPORTANTE	\N	Revêtements circulations - Plafonds M1	t	4	\N	2.5.9.2	4	68
356	AMENAGEMENTS_INTERIEURS	U-REV-05	IMPORTANTE	\N	Revêtements circulations - Cloisons M2	t	4	\N	2.5.9.2	4	68
357	AMENAGEMENTS_INTERIEURS	U-REV-06	NORMALE	\N	Revêtements circulations - Sols M4	t	3	\N	2.5.9.2	4	68
358	AMENAGEMENTS_INTERIEURS	U-REV-07	IMPORTANTE	\N	Revêtements escaliers - Plafonds et murs M1	t	4	\N	2.5.9.3	4	68
359	AMENAGEMENTS_INTERIEURS	U-REV-08	NORMALE	\N	Revêtements escaliers - Marches M3	t	3	\N	2.5.9.3	4	68
360	DESENFUMAGE	U-DES-01	CRITIQUE	\N	Désenfumage mécanique des circulations horizontales avec locaux sommeil	t	5	\N	2.6.10.3	4	69
361	DESENFUMAGE	U-DES-02	CRITIQUE	\N	Désenfumage circulations quelle que soit longueur (avec sommeil)	t	5	\N	2.6.10.3	4	69
362	DESENFUMAGE	U-DES-03	CRITIQUE	\N	Désenfumage asservi à détection automatique zone sinistrée	t	5	\N	2.6.10.3	4	69
363	DESENFUMAGE	U-DES-04	CRITIQUE	\N	Halls évacuation public désenfumés	t	5	\N	2.6.10.3	4	69
364	DESENFUMAGE	U-DES-05	IMPORTANTE	\N	Désenfumage naturel autorisé si R+1 maximum	t	4	\N	2.6.10.3	4	69
365	DESENFUMAGE	U-DES-06	IMPORTANTE	\N	Ventilateurs réalimentés par groupe électrogène si existant	t	4	\N	2.6.10.3	4	69
366	DESENFUMAGE	U-DES-07	IMPORTANTE	\N	Circulations menant aux blocs opératoires désenfumées	t	4	\N	2.6.10.4	4	69
367	DESENFUMAGE	U-DES-08	IMPORTANTE	\N	Locaux > 100m² en sous-sol désenfumés	t	4	\N	2.5.10.4	4	69
368	DESENFUMAGE	U-DES-09	IMPORTANTE	\N	Locaux > 300m² en RDC et étage désenfumés	t	4	\N	2.5.10.4	4	69
369	DESENFUMAGE	U-DES-10	IMPORTANTE	\N	Locaux > 100m² sans ouverture extérieure désenfumés	t	4	\N	2.5.10.4	4	69
370	DESENFUMAGE	U-DES-11	IMPORTANTE	\N	Commande désenfumage avant extinction automatique	t	4	\N	2.5.10.3	4	69
371	DESENFUMAGE	U-DES-12	NORMALE	\N	Ventilation confort arrêtée lors désenfumage	t	3	\N	2.5.10.3	4	69
372	DESENFUMAGE	U-DES-13	IMPORTANTE	\N	Exutoires et dispositifs conformes aux normes	t	4	\N	2.5.10.2	4	69
373	DESENFUMAGE	U-DES-14	IMPORTANTE	\N	Entretien et vérifications périodiques désenfumage	t	4	\N	2.5.10.5	4	69
374	CHAUFFAGE	U-CHA-01	IMPORTANTE	\N	Systèmes chauffage centralisé conformes	t	4	\N	2.5.11	4	70
375	CHAUFFAGE	U-CHA-02	IMPORTANTE	\N	Restrictions appareils indépendants respectées	t	4	\N	2.5.11	4	70
376	INSTALLATIONS_ELECTRIQUES	U-ELE-01	CRITIQUE	\N	Installations électriques conformes aux normes	t	5	\N	2.5.12.2	4	71
377	INSTALLATIONS_ELECTRIQUES	U-ELE-02	IMPORTANTE	\N	Pas de canalisations étrangères sauf cheminements protégés CF 1h	t	4	\N	2.5.12.2	4	71
378	INSTALLATIONS_ELECTRIQUES	U-ELE-03	IMPORTANTE	\N	Installations locaux public/non-public commandées indépendamment	t	4	\N	2.5.12.2	4	71
379	INSTALLATIONS_ELECTRIQUES	U-ELE-04	CRITIQUE	\N	Source remplacement alimente éclairage sécurité	t	5	\N	2.5.12.2	4	71
380	INSTALLATIONS_ELECTRIQUES	U-ELE-05	IMPORTANTE	\N	Tension max basse tension dans locaux public	t	4	\N	2.5.12.2	4	71
381	ECLAIRAGE_SECURITE	U-ECL-01	CRITIQUE	\N	Éclairage évacuation dans locaux ≥ 50 personnes	t	5	\N	2.5.13	4	72
382	ECLAIRAGE_SECURITE	U-ECL-02	CRITIQUE	\N	Éclairage évacuation dans locaux > 300m² (étage/RDC)	t	5	\N	2.5.13	4	72
383	ECLAIRAGE_SECURITE	U-ECL-03	CRITIQUE	\N	Éclairage évacuation dans locaux > 100m² (sous-sol)	t	5	\N	2.5.13	4	72
384	ECLAIRAGE_SECURITE	U-ECL-04	CRITIQUE	\N	Éclairage ambiance si > 100 pers. (étage/RDC)	t	5	\N	2.5.13	4	72
385	ECLAIRAGE_SECURITE	U-ECL-05	CRITIQUE	\N	Éclairage ambiance si > 50 pers. (sous-sol)	t	5	\N	2.5.13	4	72
386	ECLAIRAGE_SECURITE	U-ECL-06	IMPORTANTE	\N	Flux lumineux ≥ 5 lm/m² pour éclairage ambiance	t	4	\N	2.5.13	4	72
387	ECLAIRAGE_SECURITE	U-ECL-07	IMPORTANTE	\N	Éclairage sécurité par source centralisée ou blocs autonomes	t	4	\N	2.5.13	4	72
388	COLONNES_SECHES	U-COL-01	CRITIQUE	\N	Colonnes sèches si plancher > 18m	t	5	\N	2.5.14.1	4	73
389	COLONNES_SECHES	U-COL-02	CRITIQUE	\N	Colonne sèche dans escaliers sous-sols > 1 niveau	t	5	\N	2.6.10.6	4	73
390	COLONNES_SECHES	U-COL-03	CRITIQUE	\N	Colonnes sèches conformes au Livre 1	t	5	\N	2.5.14.1	4	73
391	ROBINETS_INCENDIE_ARMEE	U-RIA-01	CRITIQUE	\N	RIA dans établissements 1ère catégorie	t	5	\N	2.6.10.6	4	74
392	ROBINETS_INCENDIE_ARMEE	U-RIA-02	IMPORTANTE	\N	RIA dans zones accès difficile	t	4	\N	2.6.10.6	4	74
393	ROBINETS_INCENDIE_ARMEE	U-RIA-03	IMPORTANTE	\N	RIA dans bâtiments distribution compliquée	t	4	\N	2.6.10.6	4	74
394	EXTINCTEURS	U-EXT-01	IMPORTANTE	\N	Extincteurs portables répartis judicieusement	t	4	\N	Livre 1	4	75
395	EXTINCTEURS	U-EXT-02	IMPORTANTE	\N	Extincteurs appropriés aux risques	t	4	\N	Livre 1	4	75
396	EXTINCTEURS	U-EXT-03	NORMALE	\N	Extincteurs accessibles et signalisés	t	3	\N	Livre 1	4	75
397	SYSTEME_EXTINCTION_AUTOMATIQUE	U-SPK-01	IMPORTANTE	\N	Sprinklers dans locaux haut risque si demandé	t	4	\N	2.6.10.6	4	76
398	SYSTEME_EXTINCTION_AUTOMATIQUE	U-SPK-02	IMPORTANTE	\N	Sprinklers conformes aux normes	t	4	\N	2.5.14.3	4	76
399	SYSTEME_EXTINCTION_AUTOMATIQUE	U-SPK-03	IMPORTANTE	\N	Isolation partie protégée/non protégée par sprinklers	t	4	\N	2.5.14.3	4	76
400	DETECTION_ALARME	U-SSI-01	CRITIQUE	\N	SSI catégorie A obligatoire si locaux sommeil	t	5	\N	2.6.10.6	4	77
401	DETECTION_ALARME	U-SSI-02	CRITIQUE	\N	Détecteurs dans ensemble établissement (sauf escaliers/sanitaires)	t	5	\N	2.6.10.6	4	77
402	DETECTION_ALARME	U-SSI-03	CRITIQUE	\N	Détecteurs appropriés aux risques	t	5	\N	2.6.10.6	4	77
403	DETECTION_ALARME	U-DET-01	IMPORTANTE	\N	Indicateur action détecteurs visible dans circulation (locaux sommeil)	t	4	\N	2.6.10.6	4	77
404	DETECTION_ALARME	U-DET-02	CRITIQUE	\N	Détection locaux déclenche alarme générale sélective	t	5	\N	2.6.10.6	4	77
405	DETECTION_ALARME	U-DET-03	CRITIQUE	\N	Détection locaux déclenche DAS compartimentage zone	t	5	\N	2.6.10.6	4	77
406	DETECTION_ALARME	U-DET-04	CRITIQUE	\N	Détection locaux déclenche déverrouillage portes	t	5	\N	2.6.10.6	4	77
407	DETECTION_ALARME	U-DET-05	IMPORTANTE	\N	Détection locaux - Non-arrêt ascenseurs zone sinistrée	t	4	\N	2.6.10.6	4	77
408	DETECTION_ALARME	U-DET-06	CRITIQUE	\N	Détection locaux déclenche désenfumage local sinistré	t	5	\N	2.6.10.6	4	77
409	DETECTION_ALARME	U-DET-07	CRITIQUE	\N	Détection circulations déclenche alarme générale sélective	t	5	\N	2.6.10.6	4	77
410	DETECTION_ALARME	U-DET-08	CRITIQUE	\N	Détection circulations déclenche DAS compartimentage	t	5	\N	2.6.10.6	4	77
411	DETECTION_ALARME	U-DET-09	CRITIQUE	\N	Détection circulations déclenche désenfumage circulation zone	t	5	\N	2.6.10.6	4	77
412	DETECTION_ALARME	U-DET-10	IMPORTANTE	\N	Détection combles déclenche alarme générale sélective	t	4	\N	2.6.10.6	4	77
413	DETECTION_ALARME	U-UAE-01	IMPORTANTE	\N	UAE avec tableaux report si > 2500 personnes	t	4	\N	2.6.10.6	4	77
414	DETECTION_ALARME	U-UAE-02	IMPORTANTE	\N	UAE alimentée par source sécurité	t	4	\N	2.6.10.6	4	77
415	DETECTION_ALARME	U-CEN-01	NORMALE	\N	Centralisation SSI autorisée si plusieurs bâtiments	t	3	\N	2.6.10.6	4	77
416	DETECTION_ALARME	U-FER-01	CRITIQUE	\N	Fermeture simultanée portes auto asservie à détection	t	5	\N	2.6.10.5	4	77
417	DETECTION_ALARME	U-FER-02	CRITIQUE	\N	Fermeture portes recoupement au niveau sinistré	t	5	\N	2.6.10.5	4	77
418	CONSIGNES_SECURITE	U-VER-01	IMPORTANTE	\N	Services psychiatrie - Verrouillage exceptionnel autorisé	t	4	\N	2.6.10.5	4	78
419	CONSIGNES_SECURITE	U-VER-02	CRITIQUE	\N	Verrouillage - Surveillance permanente par préposé	t	5	\N	2.6.10.5	4	78
420	CONSIGNES_SECURITE	U-VER-03	CRITIQUE	\N	Interdiction clés sous verre dormant ou crémones	t	5	\N	2.6.10.5	4	78
421	CONSIGNES_SECURITE	U-VER-04	CRITIQUE	\N	Personnels dotés clés correspondantes	t	5	\N	2.6.10.5	4	78
422	CONSIGNES_SECURITE	U-VER-05	IMPORTANTE	\N	Maternités - Verrouillage exceptionnel autorisé avec surveillance	t	4	\N	2.6.10.5	4	78
423	CONSIGNES_SECURITE	U-VER-06	IMPORTANTE	\N	Établissements enfants - Verrouillage exceptionnel avec surveillance	t	4	\N	2.6.10.5	4	78
424	MOYENS_SECOURS	U-SEC-01	CRITIQUE	\N	Service sécurité assuré par agents sécurité	t	5	\N	2.6.10.6	4	79
425	MOYENS_SECOURS	U-SEC-02	CRITIQUE	\N	Surveillance permanente des bâtiments	t	5	\N	2.6.10.6	4	79
426	FORMATION_PERSONNEL	U-FOR-01	IMPORTANTE	\N	Personnel formé aux consignes sécurité	t	4	\N	Dispositions générales	4	80
427	EXERCICES_EVACUATION	U-FOR-02	IMPORTANTE	\N	Exercices évacuation réguliers	t	4	\N	Dispositions générales	4	81
428	FORMATION_PERSONNEL	U-FOR-03	IMPORTANTE	\N	Personnel formé à l'utilisation des extincteurs	t	4	\N	Dispositions générales	4	80
429	CONSIGNES_SECURITE	U-CON-06	IMPORTANTE	\N	Consignes sécurité affichées et actualisées	t	4	\N	Dispositions générales	4	78
430	PLANS_EVACUATION	U-CON-07	IMPORTANTE	\N	Plans évacuation affichés à chaque niveau	t	4	\N	Dispositions générales	4	82
431	REGISTRE_SECURITE	U-REG-01	IMPORTANTE	\N	Registre sécurité tenu à jour	t	4	\N	Dispositions générales	4	83
432	REGISTRE_SECURITE	U-REG-02	IMPORTANTE	\N	Registre contient résultats vérifications périodiques	t	4	\N	Dispositions générales	4	83
433	VERIFICATION_PERIODIQUE	U-VER-07	IMPORTANTE	\N	Vérifications périodiques installations sécurité	t	4	\N	Dispositions générales	4	84
434	VERIFICATION_PERIODIQUE	U-VER-08	IMPORTANTE	\N	Vérifications par organismes agréés	t	4	\N	Dispositions générales	4	84
435	MAINTENANCE_EQUIPEMENTS	U-MAI-01	IMPORTANTE	\N	Contrat maintenance installations sécurité	t	4	\N	Dispositions générales	4	85
436	MAINTENANCE_EQUIPEMENTS	U-MAI-02	IMPORTANTE	\N	Maintenance désenfumage	t	4	\N	2.5.10.5	4	85
437	MAINTENANCE_EQUIPEMENTS	U-MAI-03	IMPORTANTE	\N	Maintenance détection incendie	t	4	\N	Dispositions générales	4	85
438	MAINTENANCE_EQUIPEMENTS	U-MAI-04	IMPORTANTE	\N	Maintenance éclairage sécurité	t	4	\N	Dispositions générales	4	85
439	CONTROLES_REGLEMENTAIRES	U-CTR-01	IMPORTANTE	\N	Contrôles réglementaires annuels	t	4	\N	Dispositions générales	4	86
440	CONTROLES_REGLEMENTAIRES	U-CTR-02	IMPORTANTE	\N	Contrôles par commission sécurité	t	4	\N	Dispositions générales	4	86
587	ACCESSIBILITE	U-ACC-01	CRITIQUE	\N	Façade accessible avec voies échelles selon effectif	t	5	\N	2.5.1.3	6	115
588	ACCESSIBILITE	U-ACC-02	CRITIQUE	\N	Accès supplémentaire pour services de secours à tous les étages	t	5	\N	2.6.10.5	6	115
589	ACCESSIBILITE	U-ACC-03	IMPORTANTE	\N	Voies engins conformes au règlement	t	4	\N	2.5.1.1	6	115
590	ISOLEMENT_TIERS	U-ISO-01	CRITIQUE	\N	Isolement par rapport aux tiers - Distance 8m ou murs CF	t	5	\N	2.5.2	6	116
591	ISOLEMENT_TIERS	U-ISO-02	CRITIQUE	\N	Isolement des niveaux par planchers CF	t	5	\N	2.5.2	6	116
592	RESISTANCE_FEU_STRUCTURES	U-STR-01	CRITIQUE	\N	Structure SF 1h (si plancher > 8m)	t	5	\N	2.5.3	6	117
593	RESISTANCE_FEU_STRUCTURES	U-STR-02	CRITIQUE	\N	Planchers CF 1h (si plancher > 8m)	t	5	\N	2.5.3	6	117
594	FACADE	U-FAC-01	IMPORTANTE	\N	Revêtements extérieurs de façade M3	t	4	\N	2.5.4	6	118
595	FACADE	U-FAC-02	IMPORTANTE	\N	Règle C+D respectée selon masse combustible	t	4	\N	2.5.4	6	118
596	FACADE	U-FAC-03	NORMALE	\N	Menuiseries et garde-corps M3	t	3	\N	2.5.4	6	118
597	DISTRIBUTION_INTERIEURE	U-DIS-01	CRITIQUE	\N	Parois verticales CF 1h entre locaux et dégagements	t	5	\N	2.5.5.1	6	119
598	DISTRIBUTION_INTERIEURE	U-DIS-02	CRITIQUE	\N	Blocs-portes PF 1/2h	t	5	\N	2.5.5.1	6	119
599	DISTRIBUTION_INTERIEURE	U-DIS-03	IMPORTANTE	\N	Recoupement circulations horizontales tous les 25-30m par portes PF 1/2h	t	4	\N	2.5.5.1	6	119
600	DISTRIBUTION_INTERIEURE	U-ZON-01	CRITIQUE	\N	Niveaux avec locaux à sommeil divisés en zones protégées	t	5	\N	2.6.10.5	6	119
601	DISTRIBUTION_INTERIEURE	U-ZON-02	CRITIQUE	\N	Cloison CF 1h entre zones protégées de façade à façade	t	5	\N	2.6.10.5	6	119
602	DISTRIBUTION_INTERIEURE	U-ZON-03	IMPORTANTE	\N	Zones protégées de capacité d'accueil similaire	t	4	\N	2.6.10.5	6	119
603	DISTRIBUTION_INTERIEURE	U-ZON-04	IMPORTANTE	\N	Passage entre zones protégées uniquement par circulations	t	4	\N	2.6.10.5	6	119
604	LOCAUX_RISQUE	U-LRI-01	CRITIQUE	\N	Chaufferie > 70kW - Planchers et parois CF 2h	t	5	\N	2.5.6.1	6	120
605	LOCAUX_RISQUE	U-LRI-02	CRITIQUE	\N	Chaufferie > 70kW - Portes CF 1h vers sortie avec ferme-porte	t	5	\N	2.5.6.1	6	120
606	LOCAUX_RISQUE	U-LRI-03	CRITIQUE	\N	Chaufferie > 70kW - Pas de communication directe avec public	t	5	\N	2.5.6.1	6	120
607	LOCAUX_RISQUE	U-LRI-04	CRITIQUE	\N	Locaux groupes électrogènes - CF 2h	t	5	\N	2.5.6.1	6	120
608	LOCAUX_RISQUE	U-LRI-05	CRITIQUE	\N	Postes transformation électrique - CF 2h	t	5	\N	2.5.6.1	6	120
609	LOCAUX_RISQUE	U-LRI-06	CRITIQUE	\N	Locaux vide-ordures - CF 2h	t	5	\N	2.5.6.1	6	120
610	LOCAUX_RISQUE	U-LRM-01	IMPORTANTE	\N	Chaufferie 30-70kW - Planchers et parois CF 1h	t	4	\N	2.5.6.1	6	120
611	LOCAUX_RISQUE	U-LRM-02	IMPORTANTE	\N	Chaufferie 30-70kW - Portes CF 1/2h avec ferme-porte	t	4	\N	2.5.6.1	6	120
612	LOCAUX_RISQUE	U-LRM-03	IMPORTANTE	\N	Machineries ascenseurs - Isolement CF 1h	t	4	\N	2.5.6.1	6	120
613	LOCAUX_RISQUE	U-LRM-04	IMPORTANTE	\N	Locaux VMC - Isolement CF 1h	t	4	\N	2.5.6.1	6	120
614	LOCAUX_RISQUE	U-LRM-05	IMPORTANTE	\N	Cuisines collectives > 20kW - Isolement CF 1h	t	4	\N	2.5.6.1	6	120
615	LOCAUX_RISQUE	U-LRM-06	IMPORTANTE	\N	Lingeries et blanchisseries - Isolement CF 1h	t	4	\N	2.5.6.1	6	120
616	LOCAUX_RISQUE	U-LRM-07	IMPORTANTE	\N	Bagageries - Isolement CF 1h	t	4	\N	2.5.6.1	6	120
617	LOCAUX_RISQUE	U-LRM-08	IMPORTANTE	\N	Ateliers maintenance - Isolement CF 1h	t	4	\N	2.5.6.1	6	120
618	LOCAUX_RISQUE	U-LRM-09	IMPORTANTE	\N	Dépôts produits inflammables - Isolement CF 1h	t	4	\N	2.5.6.1	6	120
619	LOCAUX_RISQUE	U-LOG-01	IMPORTANTE	\N	Logements personnel - Isolement selon locaux sommeil	t	4	\N	2.5.6.2	6	120
620	CONDUITS_GAINES	U-CON-01	IMPORTANTE	\N	Conduits Ø > 75mm - Résistance au feu conforme	t	4	\N	2.5.7	6	121
621	CONDUITS_GAINES	U-CON-02	IMPORTANTE	\N	Conduits aérauliques en matériaux M0	t	4	\N	2.5.7	6	121
622	CONDUITS_GAINES	U-CON-03	IMPORTANTE	\N	Gaines techniques CF de traversée = CF plancher (max 1h)	t	4	\N	2.5.7.2	6	121
623	CONDUITS_GAINES	U-CON-04	IMPORTANTE	\N	Gaines verticales recoupées tous les 2 niveaux	t	4	\N	2.5.7.2	6	121
624	CONDUITS_GAINES	U-CON-05	NORMALE	\N	Trappes de visite des gaines PF 1/2h	t	3	\N	2.5.7.2	6	121
625	DEGAGEMENTS	U-DEG-01	CRITIQUE	\N	Circulations horizontales largeur minimale 2 UP (1,40m)	t	5	\N	2.6.10.5	6	122
626	DEGAGEMENTS	U-DEG-02	CRITIQUE	\N	Distance maximale 40m vers escalier	t	5	\N	2.6.10.5	6	122
627	DEGAGEMENTS	U-DEG-03	CRITIQUE	\N	Distance maximale 30m en cul-de-sac	t	5	\N	2.6.10.5	6	122
628	DEGAGEMENTS	U-DEG-04	CRITIQUE	\N	Portes locaux > 50 personnes s'ouvrent vers sortie	t	5	\N	2.5.8.4	6	122
629	DEGAGEMENTS	U-DEG-05	CRITIQUE	\N	Ouverture portes par simple poussée ou dispositif facile	t	5	\N	2.5.8.4	6	122
630	DEGAGEMENTS	U-DEG-06	CRITIQUE	\N	Portes recoupement en va-et-vient	t	5	\N	2.6.10.5	6	122
631	DEGAGEMENTS	U-DEG-07	CRITIQUE	\N	Calcul nombre dégagements selon effectif réglementaire	t	5	\N	2.5.8.6	6	122
632	DEGAGEMENTS	U-DEG-08	CRITIQUE	\N	Largeur dégagements conforme au tableau réglementaire	t	5	\N	2.5.8.6	6	122
633	DEGAGEMENTS	U-DEG-09	IMPORTANTE	\N	Absence de marches isolées dans circulations principales	t	4	\N	2.5.8.1	6	122
634	DEGAGEMENTS	U-DEG-10	NORMALE	\N	Portes cul-de-sac signalées "Sans issue"	t	3	\N	2.5.8.4	6	122
635	ESCALIERS	U-ESC-01	CRITIQUE	\N	Tous les escaliers sont protégés (encloisonnés)	t	5	\N	2.6.10.5	6	123
636	ESCALIERS	U-ESC-02	CRITIQUE	\N	Escaliers desservant malades non-autonomes - Largeur 2 UP minimum	t	5	\N	2.6.10.5	6	123
637	ESCALIERS	U-ESC-03	CRITIQUE	\N	Escaliers continus jusqu'au niveau d'évacuation	t	5	\N	2.5.8.5	6	123
638	ESCALIERS	U-ESC-04	CRITIQUE	\N	Séparation escaliers étages/sous-sols contre fumées	t	5	\N	2.5.8.5	6	123
639	ESCALIERS	U-ESC-05	CRITIQUE	\N	Portes escaliers PF 1/2h avec ferme-porte	t	5	\N	2.5.8.5	6	123
640	ESCALIERS	U-ESC-06	IMPORTANTE	\N	Répartition escaliers - Distance minimale 5m	t	4	\N	2.5.8.5	6	123
641	ESCALIERS	U-ESC-07	NORMALE	\N	Porte accès escalier 2 UP peut être de 1 UP	t	3	\N	2.6.10.5	6	123
642	AMENAGEMENTS_INTERIEURS	U-AME-01	NORMALE	\N	Gros mobilier et rayonnages en M3	t	3	\N	2.5.9	6	124
643	AMENAGEMENTS_INTERIEURS	U-AME-02	NORMALE	\N	Cloisons mobiles en M3	t	3	\N	2.5.9	6	124
644	AMENAGEMENTS_INTERIEURS	U-REV-01	IMPORTANTE	\N	Revêtements locaux - Plafonds M1	t	4	\N	2.5.9.1	6	124
645	AMENAGEMENTS_INTERIEURS	U-REV-02	IMPORTANTE	\N	Revêtements locaux - Parois M2	t	4	\N	2.5.9.1	6	124
646	AMENAGEMENTS_INTERIEURS	U-REV-03	NORMALE	\N	Revêtements locaux - Sols M4	t	3	\N	2.5.9.1	6	124
647	AMENAGEMENTS_INTERIEURS	U-REV-04	IMPORTANTE	\N	Revêtements circulations - Plafonds M1	t	4	\N	2.5.9.2	6	124
648	AMENAGEMENTS_INTERIEURS	U-REV-05	IMPORTANTE	\N	Revêtements circulations - Cloisons M2	t	4	\N	2.5.9.2	6	124
649	AMENAGEMENTS_INTERIEURS	U-REV-06	NORMALE	\N	Revêtements circulations - Sols M4	t	3	\N	2.5.9.2	6	124
650	AMENAGEMENTS_INTERIEURS	U-REV-07	IMPORTANTE	\N	Revêtements escaliers - Plafonds et murs M1	t	4	\N	2.5.9.3	6	124
651	AMENAGEMENTS_INTERIEURS	U-REV-08	NORMALE	\N	Revêtements escaliers - Marches M3	t	3	\N	2.5.9.3	6	124
652	DESENFUMAGE	U-DES-01	CRITIQUE	\N	Désenfumage mécanique des circulations horizontales avec locaux sommeil	t	5	\N	2.6.10.3	6	125
653	DESENFUMAGE	U-DES-02	CRITIQUE	\N	Désenfumage circulations quelle que soit longueur (avec sommeil)	t	5	\N	2.6.10.3	6	125
654	DESENFUMAGE	U-DES-03	CRITIQUE	\N	Désenfumage asservi à détection automatique zone sinistrée	t	5	\N	2.6.10.3	6	125
655	DESENFUMAGE	U-DES-04	CRITIQUE	\N	Halls évacuation public désenfumés	t	5	\N	2.6.10.3	6	125
656	DESENFUMAGE	U-DES-05	IMPORTANTE	\N	Désenfumage naturel autorisé si R+1 maximum	t	4	\N	2.6.10.3	6	125
657	DESENFUMAGE	U-DES-06	IMPORTANTE	\N	Ventilateurs réalimentés par groupe électrogène si existant	t	4	\N	2.6.10.3	6	125
658	DESENFUMAGE	U-DES-07	IMPORTANTE	\N	Circulations menant aux blocs opératoires désenfumées	t	4	\N	2.6.10.4	6	125
659	DESENFUMAGE	U-DES-08	IMPORTANTE	\N	Locaux > 100m² en sous-sol désenfumés	t	4	\N	2.5.10.4	6	125
660	DESENFUMAGE	U-DES-09	IMPORTANTE	\N	Locaux > 300m² en RDC et étage désenfumés	t	4	\N	2.5.10.4	6	125
661	DESENFUMAGE	U-DES-10	IMPORTANTE	\N	Locaux > 100m² sans ouverture extérieure désenfumés	t	4	\N	2.5.10.4	6	125
662	DESENFUMAGE	U-DES-11	IMPORTANTE	\N	Commande désenfumage avant extinction automatique	t	4	\N	2.5.10.3	6	125
663	DESENFUMAGE	U-DES-12	NORMALE	\N	Ventilation confort arrêtée lors désenfumage	t	3	\N	2.5.10.3	6	125
664	DESENFUMAGE	U-DES-13	IMPORTANTE	\N	Exutoires et dispositifs conformes aux normes	t	4	\N	2.5.10.2	6	125
665	DESENFUMAGE	U-DES-14	IMPORTANTE	\N	Entretien et vérifications périodiques désenfumage	t	4	\N	2.5.10.5	6	125
666	CHAUFFAGE	U-CHA-01	IMPORTANTE	\N	Systèmes chauffage centralisé conformes	t	4	\N	2.5.11	6	126
667	CHAUFFAGE	U-CHA-02	IMPORTANTE	\N	Restrictions appareils indépendants respectées	t	4	\N	2.5.11	6	126
668	INSTALLATIONS_ELECTRIQUES	U-ELE-01	CRITIQUE	\N	Installations électriques conformes aux normes	t	5	\N	2.5.12.2	6	127
669	INSTALLATIONS_ELECTRIQUES	U-ELE-02	IMPORTANTE	\N	Pas de canalisations étrangères sauf cheminements protégés CF 1h	t	4	\N	2.5.12.2	6	127
670	INSTALLATIONS_ELECTRIQUES	U-ELE-03	IMPORTANTE	\N	Installations locaux public/non-public commandées indépendamment	t	4	\N	2.5.12.2	6	127
671	INSTALLATIONS_ELECTRIQUES	U-ELE-04	CRITIQUE	\N	Source remplacement alimente éclairage sécurité	t	5	\N	2.5.12.2	6	127
672	INSTALLATIONS_ELECTRIQUES	U-ELE-05	IMPORTANTE	\N	Tension max basse tension dans locaux public	t	4	\N	2.5.12.2	6	127
673	ECLAIRAGE_SECURITE	U-ECL-01	CRITIQUE	\N	Éclairage évacuation dans locaux ≥ 50 personnes	t	5	\N	2.5.13	6	128
674	ECLAIRAGE_SECURITE	U-ECL-02	CRITIQUE	\N	Éclairage évacuation dans locaux > 300m² (étage/RDC)	t	5	\N	2.5.13	6	128
675	ECLAIRAGE_SECURITE	U-ECL-03	CRITIQUE	\N	Éclairage évacuation dans locaux > 100m² (sous-sol)	t	5	\N	2.5.13	6	128
676	ECLAIRAGE_SECURITE	U-ECL-04	CRITIQUE	\N	Éclairage ambiance si > 100 pers. (étage/RDC)	t	5	\N	2.5.13	6	128
677	ECLAIRAGE_SECURITE	U-ECL-05	CRITIQUE	\N	Éclairage ambiance si > 50 pers. (sous-sol)	t	5	\N	2.5.13	6	128
678	ECLAIRAGE_SECURITE	U-ECL-06	IMPORTANTE	\N	Flux lumineux ≥ 5 lm/m² pour éclairage ambiance	t	4	\N	2.5.13	6	128
679	ECLAIRAGE_SECURITE	U-ECL-07	IMPORTANTE	\N	Éclairage sécurité par source centralisée ou blocs autonomes	t	4	\N	2.5.13	6	128
680	COLONNES_SECHES	U-COL-01	CRITIQUE	\N	Colonnes sèches si plancher > 18m	t	5	\N	2.5.14.1	6	129
681	COLONNES_SECHES	U-COL-02	CRITIQUE	\N	Colonne sèche dans escaliers sous-sols > 1 niveau	t	5	\N	2.6.10.6	6	129
682	COLONNES_SECHES	U-COL-03	CRITIQUE	\N	Colonnes sèches conformes au Livre 1	t	5	\N	2.5.14.1	6	129
683	ROBINETS_INCENDIE_ARMEE	U-RIA-01	CRITIQUE	\N	RIA dans établissements 1ère catégorie	t	5	\N	2.6.10.6	6	130
684	ROBINETS_INCENDIE_ARMEE	U-RIA-02	IMPORTANTE	\N	RIA dans zones accès difficile	t	4	\N	2.6.10.6	6	130
685	ROBINETS_INCENDIE_ARMEE	U-RIA-03	IMPORTANTE	\N	RIA dans bâtiments distribution compliquée	t	4	\N	2.6.10.6	6	130
686	EXTINCTEURS	U-EXT-01	IMPORTANTE	\N	Extincteurs portables répartis judicieusement	t	4	\N	Livre 1	6	131
687	EXTINCTEURS	U-EXT-02	IMPORTANTE	\N	Extincteurs appropriés aux risques	t	4	\N	Livre 1	6	131
688	EXTINCTEURS	U-EXT-03	NORMALE	\N	Extincteurs accessibles et signalisés	t	3	\N	Livre 1	6	131
689	SYSTEME_EXTINCTION_AUTOMATIQUE	U-SPK-01	IMPORTANTE	\N	Sprinklers dans locaux haut risque si demandé	t	4	\N	2.6.10.6	6	132
690	SYSTEME_EXTINCTION_AUTOMATIQUE	U-SPK-02	IMPORTANTE	\N	Sprinklers conformes aux normes	t	4	\N	2.5.14.3	6	132
691	SYSTEME_EXTINCTION_AUTOMATIQUE	U-SPK-03	IMPORTANTE	\N	Isolation partie protégée/non protégée par sprinklers	t	4	\N	2.5.14.3	6	132
692	DETECTION_ALARME	U-SSI-01	CRITIQUE	\N	SSI catégorie A obligatoire si locaux sommeil	t	5	\N	2.6.10.6	6	133
693	DETECTION_ALARME	U-SSI-02	CRITIQUE	\N	Détecteurs dans ensemble établissement (sauf escaliers/sanitaires)	t	5	\N	2.6.10.6	6	133
694	DETECTION_ALARME	U-SSI-03	CRITIQUE	\N	Détecteurs appropriés aux risques	t	5	\N	2.6.10.6	6	133
695	DETECTION_ALARME	U-DET-01	IMPORTANTE	\N	Indicateur action détecteurs visible dans circulation (locaux sommeil)	t	4	\N	2.6.10.6	6	133
696	DETECTION_ALARME	U-DET-02	CRITIQUE	\N	Détection locaux déclenche alarme générale sélective	t	5	\N	2.6.10.6	6	133
697	DETECTION_ALARME	U-DET-03	CRITIQUE	\N	Détection locaux déclenche DAS compartimentage zone	t	5	\N	2.6.10.6	6	133
698	DETECTION_ALARME	U-DET-04	CRITIQUE	\N	Détection locaux déclenche déverrouillage portes	t	5	\N	2.6.10.6	6	133
699	DETECTION_ALARME	U-DET-05	IMPORTANTE	\N	Détection locaux - Non-arrêt ascenseurs zone sinistrée	t	4	\N	2.6.10.6	6	133
700	DETECTION_ALARME	U-DET-06	CRITIQUE	\N	Détection locaux déclenche désenfumage local sinistré	t	5	\N	2.6.10.6	6	133
701	DETECTION_ALARME	U-DET-07	CRITIQUE	\N	Détection circulations déclenche alarme générale sélective	t	5	\N	2.6.10.6	6	133
702	DETECTION_ALARME	U-DET-08	CRITIQUE	\N	Détection circulations déclenche DAS compartimentage	t	5	\N	2.6.10.6	6	133
703	DETECTION_ALARME	U-DET-09	CRITIQUE	\N	Détection circulations déclenche désenfumage circulation zone	t	5	\N	2.6.10.6	6	133
704	DETECTION_ALARME	U-DET-10	IMPORTANTE	\N	Détection combles déclenche alarme générale sélective	t	4	\N	2.6.10.6	6	133
705	DETECTION_ALARME	U-UAE-01	IMPORTANTE	\N	UAE avec tableaux report si > 2500 personnes	t	4	\N	2.6.10.6	6	133
706	DETECTION_ALARME	U-UAE-02	IMPORTANTE	\N	UAE alimentée par source sécurité	t	4	\N	2.6.10.6	6	133
707	DETECTION_ALARME	U-CEN-01	NORMALE	\N	Centralisation SSI autorisée si plusieurs bâtiments	t	3	\N	2.6.10.6	6	133
708	DETECTION_ALARME	U-FER-01	CRITIQUE	\N	Fermeture simultanée portes auto asservie à détection	t	5	\N	2.6.10.5	6	133
709	DETECTION_ALARME	U-FER-02	CRITIQUE	\N	Fermeture portes recoupement au niveau sinistré	t	5	\N	2.6.10.5	6	133
710	CONSIGNES_SECURITE	U-VER-01	IMPORTANTE	\N	Services psychiatrie - Verrouillage exceptionnel autorisé	t	4	\N	2.6.10.5	6	134
711	CONSIGNES_SECURITE	U-VER-02	CRITIQUE	\N	Verrouillage - Surveillance permanente par préposé	t	5	\N	2.6.10.5	6	134
712	CONSIGNES_SECURITE	U-VER-03	CRITIQUE	\N	Interdiction clés sous verre dormant ou crémones	t	5	\N	2.6.10.5	6	134
713	CONSIGNES_SECURITE	U-VER-04	CRITIQUE	\N	Personnels dotés clés correspondantes	t	5	\N	2.6.10.5	6	134
714	CONSIGNES_SECURITE	U-VER-05	IMPORTANTE	\N	Maternités - Verrouillage exceptionnel autorisé avec surveillance	t	4	\N	2.6.10.5	6	134
715	CONSIGNES_SECURITE	U-VER-06	IMPORTANTE	\N	Établissements enfants - Verrouillage exceptionnel avec surveillance	t	4	\N	2.6.10.5	6	134
716	MOYENS_SECOURS	U-SEC-01	CRITIQUE	\N	Service sécurité assuré par agents sécurité	t	5	\N	2.6.10.6	6	135
717	MOYENS_SECOURS	U-SEC-02	CRITIQUE	\N	Surveillance permanente des bâtiments	t	5	\N	2.6.10.6	6	135
718	FORMATION_PERSONNEL	U-FOR-01	IMPORTANTE	\N	Personnel formé aux consignes sécurité	t	4	\N	Dispositions générales	6	136
719	EXERCICES_EVACUATION	U-FOR-02	IMPORTANTE	\N	Exercices évacuation réguliers	t	4	\N	Dispositions générales	6	137
720	FORMATION_PERSONNEL	U-FOR-03	IMPORTANTE	\N	Personnel formé à l'utilisation des extincteurs	t	4	\N	Dispositions générales	6	136
721	CONSIGNES_SECURITE	U-CON-06	IMPORTANTE	\N	Consignes sécurité affichées et actualisées	t	4	\N	Dispositions générales	6	134
722	PLANS_EVACUATION	U-CON-07	IMPORTANTE	\N	Plans évacuation affichés à chaque niveau	t	4	\N	Dispositions générales	6	138
723	REGISTRE_SECURITE	U-REG-01	IMPORTANTE	\N	Registre sécurité tenu à jour	t	4	\N	Dispositions générales	6	139
724	REGISTRE_SECURITE	U-REG-02	IMPORTANTE	\N	Registre contient résultats vérifications périodiques	t	4	\N	Dispositions générales	6	139
725	VERIFICATION_PERIODIQUE	U-VER-07	IMPORTANTE	\N	Vérifications périodiques installations sécurité	t	4	\N	Dispositions générales	6	140
726	VERIFICATION_PERIODIQUE	U-VER-08	IMPORTANTE	\N	Vérifications par organismes agréés	t	4	\N	Dispositions générales	6	140
727	MAINTENANCE_EQUIPEMENTS	U-MAI-01	IMPORTANTE	\N	Contrat maintenance installations sécurité	t	4	\N	Dispositions générales	6	141
728	MAINTENANCE_EQUIPEMENTS	U-MAI-02	IMPORTANTE	\N	Maintenance désenfumage	t	4	\N	2.5.10.5	6	141
729	MAINTENANCE_EQUIPEMENTS	U-MAI-03	IMPORTANTE	\N	Maintenance détection incendie	t	4	\N	Dispositions générales	6	141
730	MAINTENANCE_EQUIPEMENTS	U-MAI-04	IMPORTANTE	\N	Maintenance éclairage sécurité	t	4	\N	Dispositions générales	6	141
731	CONTROLES_REGLEMENTAIRES	U-CTR-01	IMPORTANTE	\N	Contrôles réglementaires annuels	t	4	\N	Dispositions générales	6	142
732	CONTROLES_REGLEMENTAIRES	U-CTR-02	IMPORTANTE	\N	Contrôles par commission sécurité	t	4	\N	Dispositions générales	6	142
\.


--
-- TOC entry 5192 (class 0 OID 16438)
-- Dependencies: 226
-- Data for Name: etablissements; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.etablissements (id, actif, adresse, capacite_accueil, code_postal, date_creation, date_modification, description, nom, nombre_etages, pays, responsable_email, responsable_nom, responsable_telephone, surface_totale, type, ville, norme_id) FROM stdin;
1	t	5 Rue de Blaye ETG 4 Bureau NW1 La Gironde Casablanca	500	20500	2026-01-09 19:31:17.818525	2026-01-09 19:32:09.008089	Le CHU Ibn Sina à Rabat est le plus grand centre hospitalo-universitaire du Maroc, agissant comme pivot de la recherche et des soins (2 347 lits)	Hopital Ibn Sina	9	Maroc	contact@wayztours.com	Hssan Belgas	0678906654	\N	ERP_TYPE_U	Casablanca	3
\.


--
-- TOC entry 5194 (class 0 OID 16453)
-- Dependencies: 228
-- Data for Name: evaluations_criteres; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.evaluations_criteres (id, action_corrective, corrigee, cout_estime, date_correction_signalee, date_echeance, date_evaluation, date_modification, observation, responsable_action, statut, urgence, audit_id, critere_id) FROM stdin;
1	\N	f	\N	\N	\N	2026-01-09 19:34:45.965735	2026-01-09 19:34:45.965735	\N	\N	\N	\N	1	149
2	\N	f	\N	\N	\N	2026-01-09 19:34:45.977503	2026-01-09 19:34:45.977503	\N	\N	\N	\N	1	150
3	\N	f	\N	\N	\N	2026-01-09 19:34:45.981138	2026-01-09 19:34:45.981138	\N	\N	\N	\N	1	151
4	\N	f	\N	\N	\N	2026-01-09 19:34:45.985184	2026-01-09 19:34:45.985184	\N	\N	\N	\N	1	204
5	\N	f	\N	\N	\N	2026-01-09 19:34:45.989713	2026-01-09 19:34:45.989713	\N	\N	\N	\N	1	205
6	\N	f	\N	\N	\N	2026-01-09 19:34:45.993854	2026-01-09 19:34:45.993854	\N	\N	\N	\N	1	269
7	\N	f	\N	\N	\N	2026-01-09 19:34:45.998606	2026-01-09 19:34:45.998606	\N	\N	\N	\N	1	228
8	\N	f	\N	\N	\N	2026-01-09 19:34:46.001176	2026-01-09 19:34:46.001176	\N	\N	\N	\N	1	229
9	\N	f	\N	\N	\N	2026-01-09 19:34:46.00458	2026-01-09 19:34:46.00458	\N	\N	\N	\N	1	242
10	\N	f	\N	\N	\N	2026-01-09 19:34:46.007891	2026-01-09 19:34:46.007891	\N	\N	\N	\N	1	243
11	\N	f	\N	\N	\N	2026-01-09 19:34:46.012497	2026-01-09 19:34:46.012497	\N	\N	\N	\N	1	244
12	\N	f	\N	\N	\N	2026-01-09 19:34:46.015509	2026-01-09 19:34:46.015509	\N	\N	\N	\N	1	182
13	\N	f	\N	\N	\N	2026-01-09 19:34:46.019492	2026-01-09 19:34:46.019492	\N	\N	\N	\N	1	183
14	\N	f	\N	\N	\N	2026-01-09 19:34:46.024915	2026-01-09 19:34:46.024915	\N	\N	\N	\N	1	184
15	\N	f	\N	\N	\N	2026-01-09 19:34:46.030245	2026-01-09 19:34:46.030245	\N	\N	\N	\N	1	185
16	\N	f	\N	\N	\N	2026-01-09 19:34:46.033273	2026-01-09 19:34:46.033273	\N	\N	\N	\N	1	186
17	\N	f	\N	\N	\N	2026-01-09 19:34:46.036272	2026-01-09 19:34:46.036272	\N	\N	\N	\N	1	283
18	\N	f	\N	\N	\N	2026-01-09 19:34:46.040277	2026-01-09 19:34:46.040277	\N	\N	\N	\N	1	284
19	\N	f	\N	\N	\N	2026-01-09 19:34:46.045297	2026-01-09 19:34:46.045297	\N	\N	\N	\N	1	293
20	\N	f	\N	\N	\N	2026-01-09 19:34:46.048318	2026-01-09 19:34:46.048318	\N	\N	\N	\N	1	294
21	\N	f	\N	\N	\N	2026-01-09 19:34:46.051551	2026-01-09 19:34:46.051551	\N	\N	\N	\N	1	187
22	\N	f	\N	\N	\N	2026-01-09 19:34:46.054859	2026-01-09 19:34:46.054859	\N	\N	\N	\N	1	188
23	\N	f	\N	\N	\N	2026-01-09 19:34:46.058058	2026-01-09 19:34:46.058058	\N	\N	\N	\N	1	189
24	\N	f	\N	\N	\N	2026-01-09 19:34:46.062072	2026-01-09 19:34:46.062072	\N	\N	\N	\N	1	190
25	\N	f	\N	\N	\N	2026-01-09 19:34:46.064072	2026-01-09 19:34:46.064072	\N	\N	\N	\N	1	191
26	\N	f	\N	\N	\N	2026-01-09 19:34:46.066114	2026-01-09 19:34:46.066114	\N	\N	\N	\N	1	192
27	\N	f	\N	\N	\N	2026-01-09 19:34:46.06811	2026-01-09 19:34:46.06811	\N	\N	\N	\N	1	193
28	\N	f	\N	\N	\N	2026-01-09 19:34:46.072086	2026-01-09 19:34:46.072086	\N	\N	\N	\N	1	194
29	\N	f	\N	\N	\N	2026-01-09 19:34:46.07408	2026-01-09 19:34:46.07408	\N	\N	\N	\N	1	195
30	\N	f	\N	\N	\N	2026-01-09 19:34:46.077172	2026-01-09 19:34:46.077172	\N	\N	\N	\N	1	196
31	\N	f	\N	\N	\N	2026-01-09 19:34:46.079611	2026-01-09 19:34:46.079611	\N	\N	\N	\N	1	214
32	\N	f	\N	\N	\N	2026-01-09 19:34:46.083553	2026-01-09 19:34:46.083553	\N	\N	\N	\N	1	215
33	\N	f	\N	\N	\N	2026-01-09 19:34:46.086632	2026-01-09 19:34:46.086632	\N	\N	\N	\N	1	216
34	\N	f	\N	\N	\N	2026-01-09 19:34:46.089758	2026-01-09 19:34:46.089758	\N	\N	\N	\N	1	217
35	\N	f	\N	\N	\N	2026-01-09 19:34:46.092431	2026-01-09 19:34:46.092431	\N	\N	\N	\N	1	218
36	\N	f	\N	\N	\N	2026-01-09 19:34:46.094905	2026-01-09 19:34:46.095958	\N	\N	\N	\N	1	219
37	\N	f	\N	\N	\N	2026-01-09 19:34:46.097968	2026-01-09 19:34:46.097968	\N	\N	\N	\N	1	220
38	\N	f	\N	\N	\N	2026-01-09 19:34:46.099954	2026-01-09 19:34:46.099954	\N	\N	\N	\N	1	221
39	\N	f	\N	\N	\N	2026-01-09 19:34:46.10393	2026-01-09 19:34:46.10393	\N	\N	\N	\N	1	222
40	\N	f	\N	\N	\N	2026-01-09 19:34:46.105974	2026-01-09 19:34:46.105974	\N	\N	\N	\N	1	223
41	\N	f	\N	\N	\N	2026-01-09 19:34:46.110343	2026-01-09 19:34:46.110343	\N	\N	\N	\N	1	224
42	\N	f	\N	\N	\N	2026-01-09 19:34:46.113381	2026-01-09 19:34:46.113381	\N	\N	\N	\N	1	225
43	\N	f	\N	\N	\N	2026-01-09 19:34:46.116758	2026-01-09 19:34:46.116758	\N	\N	\N	\N	1	226
44	\N	f	\N	\N	\N	2026-01-09 19:34:46.119241	2026-01-09 19:34:46.119241	\N	\N	\N	\N	1	227
45	\N	f	\N	\N	\N	2026-01-09 19:34:46.122981	2026-01-09 19:34:46.122981	\N	\N	\N	\N	1	257
46	\N	f	\N	\N	\N	2026-01-09 19:34:46.125111	2026-01-09 19:34:46.125111	\N	\N	\N	\N	1	258
47	\N	f	\N	\N	\N	2026-01-09 19:34:46.127204	2026-01-09 19:34:46.127204	\N	\N	\N	\N	1	259
48	\N	f	\N	\N	\N	2026-01-09 19:34:46.130173	2026-01-09 19:34:46.130173	\N	\N	\N	\N	1	260
49	\N	f	\N	\N	\N	2026-01-09 19:34:46.132149	2026-01-09 19:34:46.132149	\N	\N	\N	\N	1	261
50	\N	f	\N	\N	\N	2026-01-09 19:34:46.13515	2026-01-09 19:34:46.13515	\N	\N	\N	\N	1	262
51	\N	f	\N	\N	\N	2026-01-09 19:34:46.137149	2026-01-09 19:34:46.137149	\N	\N	\N	\N	1	263
52	\N	f	\N	\N	\N	2026-01-09 19:34:46.139167	2026-01-09 19:34:46.139167	\N	\N	\N	\N	1	264
53	\N	f	\N	\N	\N	2026-01-09 19:34:46.142175	2026-01-09 19:34:46.142175	\N	\N	\N	\N	1	265
54	\N	f	\N	\N	\N	2026-01-09 19:34:46.144174	2026-01-09 19:34:46.144174	\N	\N	\N	\N	1	266
55	\N	f	\N	\N	\N	2026-01-09 19:34:46.145186	2026-01-09 19:34:46.145186	\N	\N	\N	\N	1	159
56	\N	f	\N	\N	\N	2026-01-09 19:34:46.147186	2026-01-09 19:34:46.147186	\N	\N	\N	\N	1	160
57	\N	f	\N	\N	\N	2026-01-09 19:34:46.150187	2026-01-09 19:34:46.150187	\N	\N	\N	\N	1	161
58	\N	f	\N	\N	\N	2026-01-09 19:34:46.152221	2026-01-09 19:34:46.152221	\N	\N	\N	\N	1	235
59	\N	f	\N	\N	\N	2026-01-09 19:34:46.154208	2026-01-09 19:34:46.154208	\N	\N	\N	\N	1	236
60	\N	f	\N	\N	\N	2026-01-09 19:34:46.157399	2026-01-09 19:34:46.157399	\N	\N	\N	\N	1	237
61	\N	f	\N	\N	\N	2026-01-09 19:34:46.160204	2026-01-09 19:34:46.160204	\N	\N	\N	\N	1	238
62	\N	f	\N	\N	\N	2026-01-09 19:34:46.16222	2026-01-09 19:34:46.16222	\N	\N	\N	\N	1	239
63	\N	f	\N	\N	\N	2026-01-09 19:34:46.165222	2026-01-09 19:34:46.165222	\N	\N	\N	\N	1	240
64	\N	f	\N	\N	\N	2026-01-09 19:34:46.167219	2026-01-09 19:34:46.168221	\N	\N	\N	\N	1	241
65	\N	f	\N	\N	\N	2026-01-09 19:34:46.171849	2026-01-09 19:34:46.171849	\N	\N	\N	\N	1	230
66	\N	f	\N	\N	\N	2026-01-09 19:34:46.175293	2026-01-09 19:34:46.175293	\N	\N	\N	\N	1	231
67	\N	f	\N	\N	\N	2026-01-09 19:34:46.177288	2026-01-09 19:34:46.177288	\N	\N	\N	\N	1	232
68	\N	f	\N	\N	\N	2026-01-09 19:34:46.181295	2026-01-09 19:34:46.181295	\N	\N	\N	\N	1	233
69	\N	f	\N	\N	\N	2026-01-09 19:34:46.183328	2026-01-09 19:34:46.183328	\N	\N	\N	\N	1	234
70	\N	f	\N	\N	\N	2026-01-09 19:34:46.185315	2026-01-09 19:34:46.185315	\N	\N	\N	\N	1	197
71	\N	f	\N	\N	\N	2026-01-09 19:34:46.187436	2026-01-09 19:34:46.187436	\N	\N	\N	\N	1	198
72	\N	f	\N	\N	\N	2026-01-09 19:34:46.189955	2026-01-09 19:34:46.189955	\N	\N	\N	\N	1	199
73	\N	f	\N	\N	\N	2026-01-09 19:34:46.191367	2026-01-09 19:34:46.191367	\N	\N	\N	\N	1	200
74	\N	f	\N	\N	\N	2026-01-09 19:34:46.193441	2026-01-09 19:34:46.193441	\N	\N	\N	\N	1	201
75	\N	f	\N	\N	\N	2026-01-09 19:34:46.195433	2026-01-09 19:34:46.195433	\N	\N	\N	\N	1	202
76	\N	f	\N	\N	\N	2026-01-09 19:34:46.197428	2026-01-09 19:34:46.197428	\N	\N	\N	\N	1	203
77	\N	f	\N	\N	\N	2026-01-09 19:34:46.199427	2026-01-09 19:34:46.199427	\N	\N	\N	\N	1	248
78	\N	f	\N	\N	\N	2026-01-09 19:34:46.201519	2026-01-09 19:34:46.201519	\N	\N	\N	\N	1	249
79	\N	f	\N	\N	\N	2026-01-09 19:34:46.203448	2026-01-09 19:34:46.203448	\N	\N	\N	\N	1	250
80	\N	f	\N	\N	\N	2026-01-09 19:34:46.205444	2026-01-09 19:34:46.205444	\N	\N	\N	\N	1	156
81	\N	f	\N	\N	\N	2026-01-09 19:34:46.207491	2026-01-09 19:34:46.208492	\N	\N	\N	\N	1	157
82	\N	f	\N	\N	\N	2026-01-09 19:34:46.209491	2026-01-09 19:34:46.209491	\N	\N	\N	\N	1	158
83	\N	f	\N	\N	\N	2026-01-09 19:34:46.214038	2026-01-09 19:34:46.214038	\N	\N	\N	\N	1	270
84	\N	f	\N	\N	\N	2026-01-09 19:34:46.217054	2026-01-09 19:34:46.217054	\N	\N	\N	\N	1	271
85	\N	f	\N	\N	\N	2026-01-09 19:34:46.221056	2026-01-09 19:34:46.221056	\N	\N	\N	\N	1	280
86	\N	f	\N	\N	\N	2026-01-09 19:34:46.223597	2026-01-09 19:34:46.223597	\N	\N	\N	\N	1	281
87	\N	f	\N	\N	\N	2026-01-09 19:34:46.226581	2026-01-09 19:34:46.226581	\N	\N	\N	\N	1	282
88	\N	f	\N	\N	\N	2026-01-09 19:34:46.228638	2026-01-09 19:34:46.228638	\N	\N	\N	\N	1	152
89	\N	f	\N	\N	\N	2026-01-09 19:34:46.231607	2026-01-09 19:34:46.231607	\N	\N	\N	\N	1	153
90	\N	f	\N	\N	\N	2026-01-09 19:34:46.233681	2026-01-09 19:34:46.233681	\N	\N	\N	\N	1	181
91	\N	f	\N	\N	\N	2026-01-09 19:34:46.235626	2026-01-09 19:34:46.235626	\N	\N	\N	\N	1	166
92	\N	f	\N	\N	\N	2026-01-09 19:34:46.23897	2026-01-09 19:34:46.23897	\N	\N	\N	\N	1	167
93	\N	f	\N	\N	\N	2026-01-09 19:34:46.240972	2026-01-09 19:34:46.240972	\N	\N	\N	\N	1	168
94	\N	f	\N	\N	\N	2026-01-09 19:34:46.243146	2026-01-09 19:34:46.243146	\N	\N	\N	\N	1	169
95	\N	f	\N	\N	\N	2026-01-09 19:34:46.245258	2026-01-09 19:34:46.245258	\N	\N	\N	\N	1	170
96	\N	f	\N	\N	\N	2026-01-09 19:34:46.247193	2026-01-09 19:34:46.247193	\N	\N	\N	\N	1	171
97	\N	f	\N	\N	\N	2026-01-09 19:34:46.250204	2026-01-09 19:34:46.250204	\N	\N	\N	\N	1	172
98	\N	f	\N	\N	\N	2026-01-09 19:34:46.25219	2026-01-09 19:34:46.25219	\N	\N	\N	\N	1	173
99	\N	f	\N	\N	\N	2026-01-09 19:34:46.257569	2026-01-09 19:34:46.257569	\N	\N	\N	\N	1	174
100	\N	f	\N	\N	\N	2026-01-09 19:34:46.262153	2026-01-09 19:34:46.262153	\N	\N	\N	\N	1	175
101	\N	f	\N	\N	\N	2026-01-09 19:34:46.266184	2026-01-09 19:34:46.266184	\N	\N	\N	\N	1	176
102	\N	f	\N	\N	\N	2026-01-09 19:34:46.270145	2026-01-09 19:34:46.270145	\N	\N	\N	\N	1	177
103	\N	f	\N	\N	\N	2026-01-09 19:34:46.276184	2026-01-09 19:34:46.276184	\N	\N	\N	\N	1	178
104	\N	f	\N	\N	\N	2026-01-09 19:34:46.284241	2026-01-09 19:34:46.284241	\N	\N	\N	\N	1	179
105	\N	f	\N	\N	\N	2026-01-09 19:34:46.290202	2026-01-09 19:34:46.290202	\N	\N	\N	\N	1	180
106	\N	f	\N	\N	\N	2026-01-09 19:34:46.294534	2026-01-09 19:34:46.294534	\N	\N	\N	\N	1	289
107	\N	f	\N	\N	\N	2026-01-09 19:34:46.297585	2026-01-09 19:34:46.297585	\N	\N	\N	\N	1	290
108	\N	f	\N	\N	\N	2026-01-09 19:34:46.301113	2026-01-09 19:34:46.301113	\N	\N	\N	\N	1	291
109	\N	f	\N	\N	\N	2026-01-09 19:34:46.306869	2026-01-09 19:34:46.306869	\N	\N	\N	\N	1	292
110	\N	f	\N	\N	\N	2026-01-09 19:34:46.310099	2026-01-09 19:34:46.310099	\N	\N	\N	\N	1	285
111	\N	f	\N	\N	\N	2026-01-09 19:34:46.314112	2026-01-09 19:34:46.314112	\N	\N	\N	\N	1	286
112	\N	f	\N	\N	\N	2026-01-09 19:34:46.319097	2026-01-09 19:34:46.319097	\N	\N	\N	\N	1	206
113	\N	f	\N	\N	\N	2026-01-09 19:34:46.323312	2026-01-09 19:34:46.323312	\N	\N	\N	\N	1	207
114	\N	f	\N	\N	\N	2026-01-09 19:34:46.328576	2026-01-09 19:34:46.328576	\N	\N	\N	\N	1	208
115	\N	f	\N	\N	\N	2026-01-09 19:34:46.334415	2026-01-09 19:34:46.334415	\N	\N	\N	\N	1	209
116	\N	f	\N	\N	\N	2026-01-09 19:34:46.3403	2026-01-09 19:34:46.341348	\N	\N	\N	\N	1	210
117	\N	f	\N	\N	\N	2026-01-09 19:34:46.343805	2026-01-09 19:34:46.344853	\N	\N	\N	\N	1	211
118	\N	f	\N	\N	\N	2026-01-09 19:34:46.347852	2026-01-09 19:34:46.347852	\N	\N	\N	\N	1	212
119	\N	f	\N	\N	\N	2026-01-09 19:34:46.352421	2026-01-09 19:34:46.352421	\N	\N	\N	\N	1	213
120	\N	f	\N	\N	\N	2026-01-09 19:34:46.356453	2026-01-09 19:34:46.356453	\N	\N	\N	\N	1	245
121	\N	f	\N	\N	\N	2026-01-09 19:34:46.359909	2026-01-09 19:34:46.359909	\N	\N	\N	\N	1	246
122	\N	f	\N	\N	\N	2026-01-09 19:34:46.363536	2026-01-09 19:34:46.363536	\N	\N	\N	\N	1	247
123	\N	f	\N	\N	\N	2026-01-09 19:34:46.367542	2026-01-09 19:34:46.367542	\N	\N	\N	\N	1	278
124	\N	f	\N	\N	\N	2026-01-09 19:34:46.372133	2026-01-09 19:34:46.372133	\N	\N	\N	\N	1	279
125	\N	f	\N	\N	\N	2026-01-09 19:34:46.376136	2026-01-09 19:34:46.376136	\N	\N	\N	\N	1	251
126	\N	f	\N	\N	\N	2026-01-09 19:34:46.379141	2026-01-09 19:34:46.379141	\N	\N	\N	\N	1	252
127	\N	f	\N	\N	\N	2026-01-09 19:34:46.38212	2026-01-09 19:34:46.38212	\N	\N	\N	\N	1	253
128	\N	f	\N	\N	\N	2026-01-09 19:34:46.402183	2026-01-09 19:34:46.402183	\N	\N	\N	\N	1	254
129	\N	f	\N	\N	\N	2026-01-09 19:34:46.407596	2026-01-09 19:34:46.407596	\N	\N	\N	\N	1	255
130	\N	f	\N	\N	\N	2026-01-09 19:34:46.412174	2026-01-09 19:34:46.413171	\N	\N	\N	\N	1	256
131	\N	f	\N	\N	\N	2026-01-09 19:34:46.416318	2026-01-09 19:34:46.416318	\N	\N	\N	\N	1	154
132	\N	f	\N	\N	\N	2026-01-09 19:34:46.421315	2026-01-09 19:34:46.421315	\N	\N	\N	\N	1	155
133	\N	f	\N	\N	\N	2026-01-09 19:34:46.424403	2026-01-09 19:34:46.424403	\N	\N	\N	\N	1	267
134	\N	f	\N	\N	\N	2026-01-09 19:34:46.427406	2026-01-09 19:34:46.427406	\N	\N	\N	\N	1	268
135	\N	f	\N	\N	\N	2026-01-09 19:34:46.431461	2026-01-09 19:34:46.431461	\N	\N	\N	\N	1	272
136	\N	f	\N	\N	\N	2026-01-09 19:34:46.43447	2026-01-09 19:34:46.43447	\N	\N	\N	\N	1	273
137	\N	f	\N	\N	\N	2026-01-09 19:34:46.438471	2026-01-09 19:34:46.438471	\N	\N	\N	\N	1	274
138	\N	f	\N	\N	\N	2026-01-09 19:34:46.441016	2026-01-09 19:34:46.441016	\N	\N	\N	\N	1	275
139	\N	f	\N	\N	\N	2026-01-09 19:34:46.445053	2026-01-09 19:34:46.445053	\N	\N	\N	\N	1	276
140	\N	f	\N	\N	\N	2026-01-09 19:34:46.448053	2026-01-09 19:34:46.448053	\N	\N	\N	\N	1	277
141	\N	f	\N	\N	\N	2026-01-09 19:34:46.450048	2026-01-09 19:34:46.450048	\N	\N	\N	\N	1	287
142	\N	f	\N	\N	\N	2026-01-09 19:34:46.454938	2026-01-09 19:34:46.454938	\N	\N	\N	\N	1	288
143	\N	f	\N	\N	\N	2026-01-09 19:34:46.458589	2026-01-09 19:34:46.458589	\N	\N	\N	\N	1	162
144	\N	f	\N	\N	\N	2026-01-09 19:34:46.463387	2026-01-09 19:34:46.463387	\N	\N	\N	\N	1	163
145	\N	f	\N	\N	\N	2026-01-09 19:34:46.467777	2026-01-09 19:34:46.467777	\N	\N	\N	\N	1	164
146	\N	f	\N	\N	\N	2026-01-09 19:34:46.472412	2026-01-09 19:34:46.472412	\N	\N	\N	\N	1	165
147	\N	f	\N	\N	\N	2026-01-09 19:40:10.559345	2026-01-09 19:40:10.559345	\N	\N	CONFORME	\N	1	149
148	\N	f	\N	\N	\N	2026-01-09 19:40:10.631283	2026-01-09 19:40:10.631283	\N	\N	NON_CONFORME	\N	1	150
149	\N	f	\N	\N	\N	2026-01-09 19:40:10.657328	2026-01-09 19:40:10.657328	\N	\N	CONFORME	\N	1	151
150	\N	f	\N	\N	\N	2026-01-09 19:40:10.666255	2026-01-09 19:40:10.666255	\N	\N	PARTIELLEMENT_CONFORME	\N	1	152
151	\N	f	\N	\N	\N	2026-01-09 19:40:10.675009	2026-01-09 19:40:10.675009	\N	\N	CONFORME	\N	1	153
152	\N	f	\N	\N	\N	2026-01-09 19:40:10.683322	2026-01-09 19:40:10.683322	\N	\N	NON_CONFORME	\N	1	154
153	\N	f	\N	\N	\N	2026-01-09 19:40:10.694249	2026-01-09 19:40:10.694249	\N	\N	CONFORME	\N	1	155
154	\N	f	\N	\N	\N	2026-01-09 19:40:10.70233	2026-01-09 19:40:10.70233	\N	\N	CONFORME	\N	1	156
155	\N	f	\N	\N	\N	2026-01-09 19:40:10.710317	2026-01-09 19:40:10.710317	\N	\N	NON_CONFORME	\N	1	157
156	\N	f	\N	\N	\N	2026-01-09 19:40:10.718322	2026-01-09 19:40:10.718322	\N	\N	CONFORME	\N	1	158
157	\N	f	\N	\N	\N	2026-01-09 19:40:10.727327	2026-01-09 19:40:10.727327	\N	\N	NON_CONFORME	\N	1	159
158	\N	f	\N	\N	\N	2026-01-09 19:40:10.737323	2026-01-09 19:40:10.737323	\N	\N	NON_CONFORME	\N	1	160
159	\N	f	\N	\N	\N	2026-01-09 19:40:10.750674	2026-01-09 19:40:10.750674	\N	\N	NON_CONFORME	\N	1	161
160	\N	f	\N	\N	\N	2026-01-09 19:40:10.759772	2026-01-09 19:40:10.759772	\N	\N	CONFORME	\N	1	162
161	\N	f	\N	\N	\N	2026-01-09 19:40:10.767718	2026-01-09 19:40:10.767718	\N	\N	CONFORME	\N	1	163
162	\N	f	\N	\N	\N	2026-01-09 19:40:10.776361	2026-01-09 19:40:10.776361	\N	\N	CONFORME	\N	1	164
163	\N	f	\N	\N	\N	2026-01-09 19:40:10.785293	2026-01-09 19:40:10.785293	\N	\N	CONFORME	\N	1	165
164	\N	f	\N	\N	\N	2026-01-09 19:40:10.794374	2026-01-09 19:40:10.794374	\N	\N	CONFORME	\N	1	181
165	\N	f	\N	\N	\N	2026-01-09 19:40:10.803312	2026-01-09 19:40:10.803312	\N	\N	CONFORME	\N	1	166
166	\N	f	\N	\N	\N	2026-01-09 19:40:10.813365	2026-01-09 19:40:10.813365	\N	\N	CONFORME	\N	1	167
167	\N	f	\N	\N	\N	2026-01-09 19:40:10.823294	2026-01-09 19:40:10.823294	\N	\N	CONFORME	\N	1	168
168	\N	f	\N	\N	\N	2026-01-09 19:40:10.834288	2026-01-09 19:40:10.834288	\N	\N	CONFORME	\N	1	169
169	\N	f	\N	\N	\N	2026-01-09 19:40:10.847294	2026-01-09 19:40:10.847294	\N	\N	CONFORME	\N	1	170
170	\N	f	\N	\N	\N	2026-01-09 19:40:10.858344	2026-01-09 19:40:10.858344	\N	\N	CONFORME	\N	1	171
171	\N	f	\N	\N	\N	2026-01-09 19:40:10.870345	2026-01-09 19:40:10.870345	\N	\N	CONFORME	\N	1	172
172	\N	f	\N	\N	\N	2026-01-09 19:40:10.878896	2026-01-09 19:40:10.878896	\N	\N	CONFORME	\N	1	173
173	\N	f	\N	\N	\N	2026-01-09 19:40:10.884893	2026-01-09 19:40:10.884893	\N	\N	CONFORME	\N	1	174
174	\N	f	\N	\N	\N	2026-01-09 19:40:10.890905	2026-01-09 19:40:10.890905	\N	\N	CONFORME	\N	1	175
175	\N	f	\N	\N	\N	2026-01-09 19:40:10.895903	2026-01-09 19:40:10.895903	\N	\N	CONFORME	\N	1	176
176	\N	f	\N	\N	\N	2026-01-09 19:40:10.90189	2026-01-09 19:40:10.90189	\N	\N	CONFORME	\N	1	177
177	\N	f	\N	\N	\N	2026-01-09 19:40:10.908892	2026-01-09 19:40:10.908892	\N	\N	CONFORME	\N	1	178
178	\N	f	\N	\N	\N	2026-01-09 19:40:10.914893	2026-01-09 19:40:10.914893	\N	\N	CONFORME	\N	1	179
179	\N	f	\N	\N	\N	2026-01-09 19:40:10.921895	2026-01-09 19:40:10.921895	\N	\N	CONFORME	\N	1	180
180	\N	f	\N	\N	\N	2026-01-09 19:40:10.930893	2026-01-09 19:40:10.930893	\N	\N	CONFORME	\N	1	182
181	\N	f	\N	\N	\N	2026-01-09 19:40:10.937892	2026-01-09 19:40:10.937892	\N	\N	CONFORME	\N	1	183
182	\N	f	\N	\N	\N	2026-01-09 19:40:10.944893	2026-01-09 19:40:10.944893	\N	\N	CONFORME	\N	1	184
183	\N	f	\N	\N	\N	2026-01-09 19:40:10.950893	2026-01-09 19:40:10.950893	\N	\N	CONFORME	\N	1	185
184	\N	f	\N	\N	\N	2026-01-09 19:40:10.956911	2026-01-09 19:40:10.956911	\N	\N	CONFORME	\N	1	186
185	\N	f	\N	\N	\N	2026-01-09 19:40:10.96191	2026-01-09 19:40:10.96191	\N	\N	CONFORME	\N	1	187
186	\N	f	\N	\N	\N	2026-01-09 19:40:10.968911	2026-01-09 19:40:10.968911	\N	\N	CONFORME	\N	1	188
187	\N	f	\N	\N	\N	2026-01-09 19:40:10.976445	2026-01-09 19:40:10.976445	\N	\N	CONFORME	\N	1	189
188	\N	f	\N	\N	\N	2026-01-09 19:40:10.983445	2026-01-09 19:40:10.983445	\N	\N	CONFORME	\N	1	190
189	\N	f	\N	\N	\N	2026-01-09 19:40:10.991447	2026-01-09 19:40:10.991447	\N	\N	CONFORME	\N	1	191
190	\N	f	\N	\N	\N	2026-01-09 19:40:10.997445	2026-01-09 19:40:10.997445	\N	\N	CONFORME	\N	1	192
191	\N	f	\N	\N	\N	2026-01-09 19:40:11.004445	2026-01-09 19:40:11.004445	\N	\N	CONFORME	\N	1	193
192	\N	f	\N	\N	\N	2026-01-09 19:40:11.011445	2026-01-09 19:40:11.011445	\N	\N	CONFORME	\N	1	194
193	\N	f	\N	\N	\N	2026-01-09 19:40:11.019445	2026-01-09 19:40:11.019445	\N	\N	CONFORME	\N	1	195
194	\N	f	\N	\N	\N	2026-01-09 19:40:11.027449	2026-01-09 19:40:11.027449	\N	\N	CONFORME	\N	1	196
195	\N	f	\N	\N	\N	2026-01-09 19:40:20.455116	2026-01-09 19:40:20.455116	\N	\N	CONFORME	\N	1	149
196	\N	f	\N	\N	\N	2026-01-09 19:40:20.478944	2026-01-09 19:40:20.478944	\N	\N	NON_CONFORME	\N	1	150
197	\N	f	\N	\N	\N	2026-01-09 19:40:20.495652	2026-01-09 19:40:20.495652	\N	\N	CONFORME	\N	1	151
198	\N	f	\N	\N	\N	2026-01-09 19:40:20.505647	2026-01-09 19:40:20.505647	\N	\N	PARTIELLEMENT_CONFORME	\N	1	152
199	\N	f	\N	\N	\N	2026-01-09 19:40:20.519292	2026-01-09 19:40:20.519292	\N	\N	CONFORME	\N	1	153
200	\N	f	\N	\N	\N	2026-01-09 19:40:20.532315	2026-01-09 19:40:20.532315	\N	\N	NON_CONFORME	\N	1	154
201	\N	f	\N	\N	\N	2026-01-09 19:40:20.551563	2026-01-09 19:40:20.551563	\N	\N	CONFORME	\N	1	155
202	\N	f	\N	\N	\N	2026-01-09 19:40:20.564552	2026-01-09 19:40:20.564552	\N	\N	CONFORME	\N	1	156
203	\N	f	\N	\N	\N	2026-01-09 19:40:20.571543	2026-01-09 19:40:20.572555	\N	\N	NON_CONFORME	\N	1	157
204	\N	f	\N	\N	\N	2026-01-09 19:40:20.57914	2026-01-09 19:40:20.57914	\N	\N	CONFORME	\N	1	158
205	\N	f	\N	\N	\N	2026-01-09 19:40:20.58414	2026-01-09 19:40:20.58414	\N	\N	NON_CONFORME	\N	1	159
206	\N	f	\N	\N	\N	2026-01-09 19:40:20.592315	2026-01-09 19:40:20.592315	\N	\N	NON_CONFORME	\N	1	160
207	\N	f	\N	\N	\N	2026-01-09 19:40:20.606421	2026-01-09 19:40:20.606421	\N	\N	NON_CONFORME	\N	1	161
208	\N	f	\N	\N	\N	2026-01-09 19:40:20.613132	2026-01-09 19:40:20.613132	\N	\N	CONFORME	\N	1	162
209	\N	f	\N	\N	\N	2026-01-09 19:40:20.617132	2026-01-09 19:40:20.617132	\N	\N	CONFORME	\N	1	163
210	\N	f	\N	\N	\N	2026-01-09 19:40:20.622134	2026-01-09 19:40:20.622134	\N	\N	CONFORME	\N	1	164
211	\N	f	\N	\N	\N	2026-01-09 19:40:20.629159	2026-01-09 19:40:20.629159	\N	\N	CONFORME	\N	1	165
212	\N	f	\N	\N	\N	2026-01-09 19:40:20.663583	2026-01-09 19:40:20.663583	\N	\N	CONFORME	\N	1	181
213	\N	f	\N	\N	\N	2026-01-09 19:40:20.66959	2026-01-09 19:40:20.66959	\N	\N	CONFORME	\N	1	166
214	\N	f	\N	\N	\N	2026-01-09 19:40:20.674742	2026-01-09 19:40:20.674742	\N	\N	CONFORME	\N	1	167
215	\N	f	\N	\N	\N	2026-01-09 19:40:20.680743	2026-01-09 19:40:20.680743	\N	\N	CONFORME	\N	1	168
216	\N	f	\N	\N	\N	2026-01-09 19:40:20.685743	2026-01-09 19:40:20.685743	\N	\N	CONFORME	\N	1	169
217	\N	f	\N	\N	\N	2026-01-09 19:40:20.690736	2026-01-09 19:40:20.690736	\N	\N	CONFORME	\N	1	170
218	\N	f	\N	\N	\N	2026-01-09 19:40:20.695759	2026-01-09 19:40:20.695759	\N	\N	CONFORME	\N	1	171
219	\N	f	\N	\N	\N	2026-01-09 19:40:20.701762	2026-01-09 19:40:20.701762	\N	\N	CONFORME	\N	1	172
220	\N	f	\N	\N	\N	2026-01-09 19:40:20.708269	2026-01-09 19:40:20.708269	\N	\N	CONFORME	\N	1	173
221	\N	f	\N	\N	\N	2026-01-09 19:40:20.713294	2026-01-09 19:40:20.713294	\N	\N	CONFORME	\N	1	174
222	\N	f	\N	\N	\N	2026-01-09 19:40:20.719294	2026-01-09 19:40:20.719294	\N	\N	CONFORME	\N	1	175
223	\N	f	\N	\N	\N	2026-01-09 19:40:20.725286	2026-01-09 19:40:20.725286	\N	\N	CONFORME	\N	1	176
224	\N	f	\N	\N	\N	2026-01-09 19:40:20.731285	2026-01-09 19:40:20.731285	\N	\N	CONFORME	\N	1	177
225	\N	f	\N	\N	\N	2026-01-09 19:40:20.738292	2026-01-09 19:40:20.738292	\N	\N	CONFORME	\N	1	178
226	\N	f	\N	\N	\N	2026-01-09 19:40:20.744291	2026-01-09 19:40:20.744291	\N	\N	CONFORME	\N	1	179
227	\N	f	\N	\N	\N	2026-01-09 19:40:20.750297	2026-01-09 19:40:20.750297	\N	\N	CONFORME	\N	1	180
228	\N	f	\N	\N	\N	2026-01-09 19:40:20.757291	2026-01-09 19:40:20.757291	\N	\N	CONFORME	\N	1	182
229	\N	f	\N	\N	\N	2026-01-09 19:40:20.76329	2026-01-09 19:40:20.76329	\N	\N	CONFORME	\N	1	183
230	\N	f	\N	\N	\N	2026-01-09 19:40:20.768289	2026-01-09 19:40:20.768289	\N	\N	CONFORME	\N	1	184
231	\N	f	\N	\N	\N	2026-01-09 19:40:20.775289	2026-01-09 19:40:20.775289	\N	\N	CONFORME	\N	1	185
232	\N	f	\N	\N	\N	2026-01-09 19:40:20.782292	2026-01-09 19:40:20.782292	\N	\N	CONFORME	\N	1	186
233	\N	f	\N	\N	\N	2026-01-09 19:40:20.788288	2026-01-09 19:40:20.788288	\N	\N	CONFORME	\N	1	187
234	\N	f	\N	\N	\N	2026-01-09 19:40:20.795314	2026-01-09 19:40:20.795314	\N	\N	CONFORME	\N	1	188
235	\N	f	\N	\N	\N	2026-01-09 19:40:20.802305	2026-01-09 19:40:20.802305	\N	\N	CONFORME	\N	1	189
236	\N	f	\N	\N	\N	2026-01-09 19:40:20.807823	2026-01-09 19:40:20.807823	\N	\N	CONFORME	\N	1	190
237	\N	f	\N	\N	\N	2026-01-09 19:40:20.813839	2026-01-09 19:40:20.813839	\N	\N	CONFORME	\N	1	191
238	\N	f	\N	\N	\N	2026-01-09 19:40:20.820839	2026-01-09 19:40:20.820839	\N	\N	CONFORME	\N	1	192
239	\N	f	\N	\N	\N	2026-01-09 19:40:20.82584	2026-01-09 19:40:20.82584	\N	\N	CONFORME	\N	1	193
240	\N	f	\N	\N	\N	2026-01-09 19:40:20.83184	2026-01-09 19:40:20.83184	\N	\N	CONFORME	\N	1	194
241	\N	f	\N	\N	\N	2026-01-09 19:40:20.837841	2026-01-09 19:40:20.837841	\N	\N	CONFORME	\N	1	195
242	\N	f	\N	\N	\N	2026-01-09 19:40:20.84484	2026-01-09 19:40:20.84484	\N	\N	CONFORME	\N	1	196
\.


--
-- TOC entry 5196 (class 0 OID 16467)
-- Dependencies: 230
-- Data for Name: historique_actions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.historique_actions (id, action, adresseip, anciens_valeurs, date_action, description, entite, entite_id, nouvelles_valeurs, user_agent, utilisateur_id) FROM stdin;
\.


--
-- TOC entry 5197 (class 0 OID 16479)
-- Dependencies: 231
-- Data for Name: norme_types_etablissements; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.norme_types_etablissements (norme_id, type_etablissement) FROM stdin;
3	ERP_TYPE_J
4	ERP_TYPE_J
6	BH_1ERE_FAMILLE
\.


--
-- TOC entry 5199 (class 0 OID 16485)
-- Dependencies: 233
-- Data for Name: normes; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.normes (id, actif, code, date_creation, date_modification, date_vigueur, description, nom, pays, version) FROM stdin;
3	t	MAROC-GENERALE-2024	2026-01-09 18:23:28.095858	2026-01-09 18:23:28.095858	2024-01-01	Norme marocaine générale applicable à tous types d''établissements. Standards de base en matière de sécurité incendie, prévention des risques, moyens de secours et organisation de la sécurité.	Norme Générale de Sécurité Incendie	Maroc	1.0
4	t	MAROC-ERP-CAT2-2024	2026-01-09 18:59:23.43448	2026-01-09 18:59:23.435474	2026-01-09	Norme marocaine pour les établissements recevant du public de 2ème catégorie. Standards de sécurité adaptés aux établissements de taille moyenne accueillant entre 701 et 1500 personnes	Norme marocaine...	Maroc	1.0
6	t	MAROC-ERP-CAT4-2024	2026-01-09 19:23:46.880199	2026-01-09 19:27:21.321587	2024-01-01	Norme marocaine pour les établissements recevant du public de 4ème catégorie. Standards de base pour petits établissements publics accueillant moins de 300 personnes.	Norme ERP - 4ème Catégorie (< 300 personnes)	Maroc	1.0
\.


--
-- TOC entry 5201 (class 0 OID 16498)
-- Dependencies: 235
-- Data for Name: notifications; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.notifications (id, date_creation, date_lecture, lien, lu, message, titre, type, destinataire_id) FROM stdin;
1	2026-01-09 19:34:46.502312	\N	/audits/1	f	Un audit a été planifié pour l'établissement Hopital Ibn Sina le 2026-01-15	Audit planifié	AUDIT_PLANIFIE	3
2	2026-01-09 19:40:10.645285	\N	/audits/1/evaluations/148	f	Une non-conformité critique a été détectée : Accès supplémentaire pour services de secours à tous les étages	Non-conformité critique détectée	NON_CONFORMITE_CRITIQUE	3
3	2026-01-09 19:40:10.687323	\N	/audits/1/evaluations/152	f	Une non-conformité critique a été détectée : Structure SF 1h (si plancher > 8m)	Non-conformité critique détectée	NON_CONFORMITE_CRITIQUE	3
4	2026-01-09 19:40:10.730321	\N	/audits/1/evaluations/157	f	Une non-conformité critique a été détectée : Parois verticales CF 1h entre locaux et dégagements	Non-conformité critique détectée	NON_CONFORMITE_CRITIQUE	3
5	2026-01-09 19:40:10.740313	\N	/audits/1/evaluations/158	f	Une non-conformité critique a été détectée : Blocs-portes PF 1/2h	Non-conformité critique détectée	NON_CONFORMITE_CRITIQUE	3
6	2026-01-09 19:40:20.484374	\N	/audits/1/evaluations/196	f	Une non-conformité critique a été détectée : Accès supplémentaire pour services de secours à tous les étages	Non-conformité critique détectée	NON_CONFORMITE_CRITIQUE	3
7	2026-01-09 19:40:20.536297	\N	/audits/1/evaluations/200	f	Une non-conformité critique a été détectée : Structure SF 1h (si plancher > 8m)	Non-conformité critique détectée	NON_CONFORMITE_CRITIQUE	3
8	2026-01-09 19:40:20.586135	\N	/audits/1/evaluations/205	f	Une non-conformité critique a été détectée : Parois verticales CF 1h entre locaux et dégagements	Non-conformité critique détectée	NON_CONFORMITE_CRITIQUE	3
9	2026-01-09 19:40:20.594421	\N	/audits/1/evaluations/206	f	Une non-conformité critique a été détectée : Blocs-portes PF 1/2h	Non-conformité critique détectée	NON_CONFORMITE_CRITIQUE	3
\.


--
-- TOC entry 5203 (class 0 OID 16514)
-- Dependencies: 237
-- Data for Name: password_reset_tokens; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.password_reset_tokens (id, date_creation, date_expiration, date_utilisation, token, utilise, user_id) FROM stdin;
\.


--
-- TOC entry 5205 (class 0 OID 16526)
-- Dependencies: 239
-- Data for Name: photos_etablissement; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.photos_etablissement (id, chemin_fichier, date_upload, description, nom_fichier, principale, taille_fichier, etablissement_id) FROM stdin;
\.


--
-- TOC entry 5207 (class 0 OID 16540)
-- Dependencies: 241
-- Data for Name: preuves; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.preuves (id, chemin_fichier, date_upload, description, nom_fichier, taille_fichier, type_fichier, evaluation_id) FROM stdin;
\.


--
-- TOC entry 5209 (class 0 OID 16555)
-- Dependencies: 243
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.roles (id, description, name) FROM stdin;
1	Administrateur	ADMIN
2	Manager	MANAGER
3	Auditeur	AUDITOR
\.


--
-- TOC entry 5211 (class 0 OID 16564)
-- Dependencies: 245
-- Data for Name: sections; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sections (id, code, date_creation, date_modification, description, ordre, titre, norme_id) FROM stdin;
31	ACCESSIBILITE	2026-01-09 18:23:38.62993	2026-01-09 18:23:38.62993	Section regroupant les critères de type Accessibilité	1	Accessibilité	3
32	ISOLEMENT_TIERS	2026-01-09 18:23:38.639897	2026-01-09 18:23:38.639897	Section regroupant les critères de type Isolement par rapport aux tiers	2	Isolement par rapport aux tiers	3
33	RESISTANCE_FEU_STRUCTURES	2026-01-09 18:23:38.645891	2026-01-09 18:23:38.645891	Section regroupant les critères de type Résistance au feu des structures	3	Résistance au feu des structures	3
34	FACADE	2026-01-09 18:23:38.652892	2026-01-09 18:23:38.652892	Section regroupant les critères de type Façades	4	Façades	3
35	DISTRIBUTION_INTERIEURE	2026-01-09 18:23:38.659893	2026-01-09 18:23:38.659893	Section regroupant les critères de type Distribution intérieure	5	Distribution intérieure	3
36	LOCAUX_RISQUE	2026-01-09 18:23:38.697893	2026-01-09 18:23:38.697893	Section regroupant les critères de type Locaux à risque	6	Locaux à risque	3
37	CONDUITS_GAINES	2026-01-09 18:23:38.72589	2026-01-09 18:23:38.72589	Section regroupant les critères de type Conduits et gaines	7	Conduits et gaines	3
38	DEGAGEMENTS	2026-01-09 18:23:38.735894	2026-01-09 18:23:38.735894	Section regroupant les critères de type Dégagements	8	Dégagements	3
39	ESCALIERS	2026-01-09 18:23:38.751889	2026-01-09 18:23:38.751889	Section regroupant les critères de type Escaliers	22	Escaliers	3
40	AMENAGEMENTS_INTERIEURS	2026-01-09 18:23:38.763892	2026-01-09 18:23:38.763892	Section regroupant les critères de type Aménagements intérieurs	9	Aménagements intérieurs	3
41	DESENFUMAGE	2026-01-09 18:23:38.783892	2026-01-09 18:23:38.783892	Section regroupant les critères de type Désenfumage	10	Désenfumage	3
42	CHAUFFAGE	2026-01-09 18:23:38.803891	2026-01-09 18:23:38.803891	Section regroupant les critères de type Chauffage	11	Chauffage	3
43	INSTALLATIONS_ELECTRIQUES	2026-01-09 18:23:38.808892	2026-01-09 18:23:38.808892	Section regroupant les critères de type Installations électriques	12	Installations électriques	3
44	ECLAIRAGE_SECURITE	2026-01-09 18:23:38.816892	2026-01-09 18:23:38.816892	Section regroupant les critères de type Éclairage de sécurité	13	Éclairage de sécurité	3
45	COLONNES_SECHES	2026-01-09 18:23:38.827896	2026-01-09 18:23:38.827896	Section regroupant les critères de type Colonnes sèches	19	Colonnes sèches	3
46	ROBINETS_INCENDIE_ARMEE	2026-01-09 18:23:38.835889	2026-01-09 18:23:38.835889	Section regroupant les critères de type Robinets d'incendie armés	18	Robinets d'incendie armés	3
47	EXTINCTEURS	2026-01-09 18:23:38.840891	2026-01-09 18:23:38.840891	Section regroupant les critères de type Extincteurs	17	Extincteurs	3
48	SYSTEME_EXTINCTION_AUTOMATIQUE	2026-01-09 18:23:38.847893	2026-01-09 18:23:38.847893	Section regroupant les critères de type Système d'extinction automatique	16	Système d'extinction automatique	3
49	DETECTION_ALARME	2026-01-09 18:23:38.85389	2026-01-09 18:23:38.85389	Section regroupant les critères de type Détection automatique d'incendie et alarme	15	Détection automatique d'incendie et alarme	3
50	CONSIGNES_SECURITE	2026-01-09 18:23:38.88089	2026-01-09 18:23:38.88089	Section regroupant les critères de type Consignes de sécurité	38	Consignes de sécurité	3
51	MOYENS_SECOURS	2026-01-09 18:23:38.890887	2026-01-09 18:23:38.890887	Section regroupant les critères de type Moyens de secours	14	Moyens de secours	3
52	FORMATION_PERSONNEL	2026-01-09 18:23:38.893891	2026-01-09 18:23:38.893891	Section regroupant les critères de type Formation du personnel	36	Formation du personnel	3
53	EXERCICES_EVACUATION	2026-01-09 18:23:38.895896	2026-01-09 18:23:38.895896	Section regroupant les critères de type Exercices d'évacuation	37	Exercices d'évacuation	3
54	PLANS_EVACUATION	2026-01-09 18:23:38.901888	2026-01-09 18:23:38.901888	Section regroupant les critères de type Plans d'évacuation	40	Plans d'évacuation	3
55	REGISTRE_SECURITE	2026-01-09 18:23:38.903893	2026-01-09 18:23:38.903893	Section regroupant les critères de type Registre de sécurité	41	Registre de sécurité	3
56	VERIFICATION_PERIODIQUE	2026-01-09 18:23:38.907889	2026-01-09 18:23:38.907889	Section regroupant les critères de type Vérifications périodiques	42	Vérifications périodiques	3
57	MAINTENANCE_EQUIPEMENTS	2026-01-09 18:23:38.910892	2026-01-09 18:23:38.910892	Section regroupant les critères de type Maintenance des équipements	43	Maintenance des équipements	3
58	CONTROLES_REGLEMENTAIRES	2026-01-09 18:23:38.915893	2026-01-09 18:23:38.915893	Section regroupant les critères de type Contrôles réglementaires	44	Contrôles réglementaires	3
59	ACCESSIBILITE	2026-01-09 19:04:12.996717	2026-01-09 19:04:12.996717	Section regroupant les critères de type Accessibilité	1	Accessibilité	4
60	ISOLEMENT_TIERS	2026-01-09 19:04:13.066977	2026-01-09 19:04:13.066977	Section regroupant les critères de type Isolement par rapport aux tiers	2	Isolement par rapport aux tiers	4
61	RESISTANCE_FEU_STRUCTURES	2026-01-09 19:04:13.118476	2026-01-09 19:04:13.118476	Section regroupant les critères de type Résistance au feu des structures	3	Résistance au feu des structures	4
62	FACADE	2026-01-09 19:04:13.173772	2026-01-09 19:04:13.173772	Section regroupant les critères de type Façades	4	Façades	4
63	DISTRIBUTION_INTERIEURE	2026-01-09 19:04:13.204567	2026-01-09 19:04:13.205566	Section regroupant les critères de type Distribution intérieure	5	Distribution intérieure	4
64	LOCAUX_RISQUE	2026-01-09 19:04:13.274807	2026-01-09 19:04:13.274807	Section regroupant les critères de type Locaux à risque	6	Locaux à risque	4
65	CONDUITS_GAINES	2026-01-09 19:04:13.442977	2026-01-09 19:04:13.442977	Section regroupant les critères de type Conduits et gaines	7	Conduits et gaines	4
66	DEGAGEMENTS	2026-01-09 19:04:13.488958	2026-01-09 19:04:13.488958	Section regroupant les critères de type Dégagements	8	Dégagements	4
67	ESCALIERS	2026-01-09 19:04:13.592205	2026-01-09 19:04:13.592205	Section regroupant les critères de type Escaliers	22	Escaliers	4
68	AMENAGEMENTS_INTERIEURS	2026-01-09 19:04:13.648678	2026-01-09 19:04:13.648678	Section regroupant les critères de type Aménagements intérieurs	9	Aménagements intérieurs	4
69	DESENFUMAGE	2026-01-09 19:04:13.734067	2026-01-09 19:04:13.734067	Section regroupant les critères de type Désenfumage	10	Désenfumage	4
70	CHAUFFAGE	2026-01-09 19:04:13.831276	2026-01-09 19:04:13.831276	Section regroupant les critères de type Chauffage	11	Chauffage	4
71	INSTALLATIONS_ELECTRIQUES	2026-01-09 19:04:13.852966	2026-01-09 19:04:13.852966	Section regroupant les critères de type Installations électriques	12	Installations électriques	4
72	ECLAIRAGE_SECURITE	2026-01-09 19:04:13.885805	2026-01-09 19:04:13.885805	Section regroupant les critères de type Éclairage de sécurité	13	Éclairage de sécurité	4
73	COLONNES_SECHES	2026-01-09 19:04:13.933409	2026-01-09 19:04:13.933409	Section regroupant les critères de type Colonnes sèches	19	Colonnes sèches	4
74	ROBINETS_INCENDIE_ARMEE	2026-01-09 19:04:13.95957	2026-01-09 19:04:13.95957	Section regroupant les critères de type Robinets d'incendie armés	18	Robinets d'incendie armés	4
75	EXTINCTEURS	2026-01-09 19:04:13.99503	2026-01-09 19:04:13.99503	Section regroupant les critères de type Extincteurs	17	Extincteurs	4
76	SYSTEME_EXTINCTION_AUTOMATIQUE	2026-01-09 19:04:14.016848	2026-01-09 19:04:14.016848	Section regroupant les critères de type Système d'extinction automatique	16	Système d'extinction automatique	4
77	DETECTION_ALARME	2026-01-09 19:04:14.051783	2026-01-09 19:04:14.051783	Section regroupant les critères de type Détection automatique d'incendie et alarme	15	Détection automatique d'incendie et alarme	4
78	CONSIGNES_SECURITE	2026-01-09 19:04:14.177179	2026-01-09 19:04:14.177179	Section regroupant les critères de type Consignes de sécurité	38	Consignes de sécurité	4
79	MOYENS_SECOURS	2026-01-09 19:04:14.220555	2026-01-09 19:04:14.220555	Section regroupant les critères de type Moyens de secours	14	Moyens de secours	4
80	FORMATION_PERSONNEL	2026-01-09 19:04:14.239457	2026-01-09 19:04:14.239457	Section regroupant les critères de type Formation du personnel	36	Formation du personnel	4
81	EXERCICES_EVACUATION	2026-01-09 19:04:14.249805	2026-01-09 19:04:14.249805	Section regroupant les critères de type Exercices d'évacuation	37	Exercices d'évacuation	4
82	PLANS_EVACUATION	2026-01-09 19:04:14.270768	2026-01-09 19:04:14.270768	Section regroupant les critères de type Plans d'évacuation	40	Plans d'évacuation	4
83	REGISTRE_SECURITE	2026-01-09 19:04:14.278625	2026-01-09 19:04:14.278625	Section regroupant les critères de type Registre de sécurité	41	Registre de sécurité	4
84	VERIFICATION_PERIODIQUE	2026-01-09 19:04:14.292829	2026-01-09 19:04:14.292829	Section regroupant les critères de type Vérifications périodiques	42	Vérifications périodiques	4
85	MAINTENANCE_EQUIPEMENTS	2026-01-09 19:04:14.310503	2026-01-09 19:04:14.310503	Section regroupant les critères de type Maintenance des équipements	43	Maintenance des équipements	4
86	CONTROLES_REGLEMENTAIRES	2026-01-09 19:04:14.329699	2026-01-09 19:04:14.329699	Section regroupant les critères de type Contrôles réglementaires	44	Contrôles réglementaires	4
115	ACCESSIBILITE	2026-01-09 19:26:33.876831	2026-01-09 19:26:33.876831	Section regroupant les critères de type Accessibilité	1	Accessibilité	6
116	ISOLEMENT_TIERS	2026-01-09 19:26:33.900536	2026-01-09 19:26:33.900536	Section regroupant les critères de type Isolement par rapport aux tiers	2	Isolement par rapport aux tiers	6
117	RESISTANCE_FEU_STRUCTURES	2026-01-09 19:26:33.91346	2026-01-09 19:26:33.91346	Section regroupant les critères de type Résistance au feu des structures	3	Résistance au feu des structures	6
118	FACADE	2026-01-09 19:26:33.924416	2026-01-09 19:26:33.924416	Section regroupant les critères de type Façades	4	Façades	6
119	DISTRIBUTION_INTERIEURE	2026-01-09 19:26:33.944654	2026-01-09 19:26:33.944654	Section regroupant les critères de type Distribution intérieure	5	Distribution intérieure	6
120	LOCAUX_RISQUE	2026-01-09 19:26:33.973267	2026-01-09 19:26:33.973267	Section regroupant les critères de type Locaux à risque	6	Locaux à risque	6
121	CONDUITS_GAINES	2026-01-09 19:26:34.05097	2026-01-09 19:26:34.052398	Section regroupant les critères de type Conduits et gaines	7	Conduits et gaines	6
122	DEGAGEMENTS	2026-01-09 19:26:34.072375	2026-01-09 19:26:34.072375	Section regroupant les critères de type Dégagements	8	Dégagements	6
123	ESCALIERS	2026-01-09 19:26:34.12093	2026-01-09 19:26:34.12093	Section regroupant les critères de type Escaliers	22	Escaliers	6
124	AMENAGEMENTS_INTERIEURS	2026-01-09 19:26:34.147901	2026-01-09 19:26:34.147901	Section regroupant les critères de type Aménagements intérieurs	9	Aménagements intérieurs	6
125	DESENFUMAGE	2026-01-09 19:26:34.184327	2026-01-09 19:26:34.184327	Section regroupant les critères de type Désenfumage	10	Désenfumage	6
126	CHAUFFAGE	2026-01-09 19:26:34.23124	2026-01-09 19:26:34.23124	Section regroupant les critères de type Chauffage	11	Chauffage	6
127	INSTALLATIONS_ELECTRIQUES	2026-01-09 19:26:34.243309	2026-01-09 19:26:34.243309	Section regroupant les critères de type Installations électriques	12	Installations électriques	6
128	ECLAIRAGE_SECURITE	2026-01-09 19:26:34.263028	2026-01-09 19:26:34.264069	Section regroupant les critères de type Éclairage de sécurité	13	Éclairage de sécurité	6
129	COLONNES_SECHES	2026-01-09 19:26:34.292292	2026-01-09 19:26:34.292292	Section regroupant les critères de type Colonnes sèches	19	Colonnes sèches	6
130	ROBINETS_INCENDIE_ARMEE	2026-01-09 19:26:34.303702	2026-01-09 19:26:34.303702	Section regroupant les critères de type Robinets d'incendie armés	18	Robinets d'incendie armés	6
131	EXTINCTEURS	2026-01-09 19:26:34.316272	2026-01-09 19:26:34.316272	Section regroupant les critères de type Extincteurs	17	Extincteurs	6
132	SYSTEME_EXTINCTION_AUTOMATIQUE	2026-01-09 19:26:34.328849	2026-01-09 19:26:34.328849	Section regroupant les critères de type Système d'extinction automatique	16	Système d'extinction automatique	6
133	DETECTION_ALARME	2026-01-09 19:26:34.344959	2026-01-09 19:26:34.344959	Section regroupant les critères de type Détection automatique d'incendie et alarme	15	Détection automatique d'incendie et alarme	6
134	CONSIGNES_SECURITE	2026-01-09 19:26:34.412268	2026-01-09 19:26:34.412268	Section regroupant les critères de type Consignes de sécurité	38	Consignes de sécurité	6
135	MOYENS_SECOURS	2026-01-09 19:26:34.436132	2026-01-09 19:26:34.436132	Section regroupant les critères de type Moyens de secours	14	Moyens de secours	6
136	FORMATION_PERSONNEL	2026-01-09 19:26:34.446823	2026-01-09 19:26:34.447824	Section regroupant les critères de type Formation du personnel	36	Formation du personnel	6
137	EXERCICES_EVACUATION	2026-01-09 19:26:34.457396	2026-01-09 19:26:34.457396	Section regroupant les critères de type Exercices d'évacuation	37	Exercices d'évacuation	6
138	PLANS_EVACUATION	2026-01-09 19:26:34.469895	2026-01-09 19:26:34.469895	Section regroupant les critères de type Plans d'évacuation	40	Plans d'évacuation	6
139	REGISTRE_SECURITE	2026-01-09 19:26:34.478777	2026-01-09 19:26:34.478777	Section regroupant les critères de type Registre de sécurité	41	Registre de sécurité	6
140	VERIFICATION_PERIODIQUE	2026-01-09 19:26:34.492233	2026-01-09 19:26:34.492233	Section regroupant les critères de type Vérifications périodiques	42	Vérifications périodiques	6
141	MAINTENANCE_EQUIPEMENTS	2026-01-09 19:26:34.499787	2026-01-09 19:26:34.499787	Section regroupant les critères de type Maintenance des équipements	43	Maintenance des équipements	6
142	CONTROLES_REGLEMENTAIRES	2026-01-09 19:26:34.531863	2026-01-09 19:26:34.531863	Section regroupant les critères de type Contrôles réglementaires	44	Contrôles réglementaires	6
\.


--
-- TOC entry 5212 (class 0 OID 16576)
-- Dependencies: 246
-- Data for Name: user_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_roles (user_id, role_id) FROM stdin;
1	1
2	2
3	3
4	3
5	3
6	3
7	3
10	3
\.


--
-- TOC entry 5214 (class 0 OID 16584)
-- Dependencies: 248
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, actif, date_creation, date_modification, derniere_connexion, email, nom, password, prenom, telephone, username) FROM stdin;
1	t	2026-01-09 17:12:14.720652	2026-01-09 17:12:14.720652	\N	admin@firesafe.com	Admin	$2a$10$FHzlqY0O/UoB/DUHAleaiemLpl8a8Dl5boxLk6ShW4am8Y8r44fNi	System	0600000000	superadmin
2	t	2026-01-09 17:12:14.867646	2026-01-09 17:12:14.867646	\N	manager@firesafe.com	Manager	$2a$10$fUVFo0.IDjKvD7ZuNQEtmOvuVroIG7vxhDXomS1O2Na3kMd732CiO	Test	0611111111	manager
3	t	2026-01-09 17:12:14.970649	2026-01-09 17:12:14.970649	\N	auditor@firesafe.com	Auditor	$2a$10$b6IMyaYDjbiHKSVMbWhFsOd/K0S3dqeSYsQ1FkiK87iml22LX.Qla	Senior	0611111111	auditor1
4	t	2026-01-09 17:12:15.087691	2026-01-09 17:12:15.087691	\N	user@firesafe.com	User	$2a$10$IjcSA9tH5JX.DW0eZob/fOcm9XT.BX8DWDZrQNJwf43pDgEdS3zQm	Regular	0611111111	user1
5	t	2026-01-09 17:12:15.185654	2026-01-09 17:12:15.185654	\N	auditor2@firesafe.com	Auditor	$2a$10$rIdRxmYvrtFkNnVglEBPpeYbW2ULQZsTAwCJ3hil.1pWvbrHVMTg6	Junior	0611111111	auditor2
6	f	2026-01-09 20:12:59.698893	2026-01-09 20:19:34.756606	\N	belgasmeriem@gmail.com	Belgas	$2a$10$PNcqsOsH8JXXoA84uXiF4u4ZkqQ09GRdFzJiTkFsv5zLOyiZoinaq	Meriem	0637335963	Mery
7	f	2026-01-09 20:27:07.337691	2026-01-09 20:28:37.945052	\N	samihachraf924@gmail.com	Samih	$2a$10$Z9eX06A/HYJuh.flyDLm8.AdK/vBJUzzlNzSgIHODioE7ffZEP5oO	Achraf	0637335963	AManager
10	t	2026-01-09 20:32:57.085704	2026-01-09 20:32:57.085704	\N	fatihanwar319@gmail.com	Fatih	$2a$10$Sv26Ww/.BtUi9A7yAys.r.aKjBAJr61VfUwT1T5SsbfFv6Gv3HPdi	Anwar	0637335963	New Manager
\.


--
-- TOC entry 5220 (class 0 OID 0)
-- Dependencies: 219
-- Name: audits_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.audits_id_seq', 1, true);


--
-- TOC entry 5221 (class 0 OID 0)
-- Dependencies: 221
-- Name: commentaires_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.commentaires_id_seq', 1, true);


--
-- TOC entry 5222 (class 0 OID 0)
-- Dependencies: 223
-- Name: criteres_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.criteres_id_seq', 732, true);


--
-- TOC entry 5223 (class 0 OID 0)
-- Dependencies: 225
-- Name: etablissements_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.etablissements_id_seq', 1, true);


--
-- TOC entry 5224 (class 0 OID 0)
-- Dependencies: 227
-- Name: evaluations_criteres_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.evaluations_criteres_id_seq', 242, true);


--
-- TOC entry 5225 (class 0 OID 0)
-- Dependencies: 229
-- Name: historique_actions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.historique_actions_id_seq', 1, false);


--
-- TOC entry 5226 (class 0 OID 0)
-- Dependencies: 232
-- Name: normes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.normes_id_seq', 6, true);


--
-- TOC entry 5227 (class 0 OID 0)
-- Dependencies: 234
-- Name: notifications_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.notifications_id_seq', 9, true);


--
-- TOC entry 5228 (class 0 OID 0)
-- Dependencies: 236
-- Name: password_reset_tokens_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.password_reset_tokens_id_seq', 1, false);


--
-- TOC entry 5229 (class 0 OID 0)
-- Dependencies: 238
-- Name: photos_etablissement_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.photos_etablissement_id_seq', 1, false);


--
-- TOC entry 5230 (class 0 OID 0)
-- Dependencies: 240
-- Name: preuves_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.preuves_id_seq', 1, false);


--
-- TOC entry 5231 (class 0 OID 0)
-- Dependencies: 242
-- Name: roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.roles_id_seq', 3, true);


--
-- TOC entry 5232 (class 0 OID 0)
-- Dependencies: 244
-- Name: sections_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sections_id_seq', 142, true);


--
-- TOC entry 5233 (class 0 OID 0)
-- Dependencies: 247
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 11, true);


-- Completed on 2026-01-09 21:35:56

--
-- PostgreSQL database dump complete
--

\unrestrict ectBapDlz4f3UaN99JzG6BWrQWQMV6Db6S3xBSjD0Crpv7zDNjztdrDJh2HjzYi

