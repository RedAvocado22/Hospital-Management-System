-- =============================================================
-- V10__seed_medicines.sql
-- Seed 100 medicines across common hospital categories.
-- =============================================================

INSERT INTO medicine (id, created_at, updated_at, name, description, price, quantity, is_active) VALUES
-- Antibiotics
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Amoxicillin 500mg', 'Broad-spectrum penicillin antibiotic for bacterial infections including respiratory tract and urinary tract infections.', 12.50, 500, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Azithromycin 250mg', 'Macrolide antibiotic used to treat a wide variety of bacterial infections including pneumonia and bronchitis.', 18.75, 300, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Ciprofloxacin 500mg', 'Fluoroquinolone antibiotic effective against gram-negative bacteria and urinary tract infections.', 15.00, 400, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Doxycycline 100mg', 'Tetracycline antibiotic used for respiratory infections, acne, and Lyme disease prophylaxis.', 9.00, 600, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Metronidazole 400mg', 'Antibiotic and antiprotozoal used for anaerobic bacterial and parasitic infections.', 7.50, 450, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Cephalexin 500mg', 'First-generation cephalosporin antibiotic for skin and soft tissue infections.', 14.00, 350, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Clindamycin 300mg', 'Lincosamide antibiotic for serious infections caused by susceptible anaerobic bacteria.', 22.00, 250, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Trimethoprim 200mg', 'Antibiotic used primarily for urinary tract infections and as prophylaxis.', 8.50, 400, 1),

-- Analgesics & NSAIDs
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Paracetamol 500mg', 'Analgesic and antipyretic for mild to moderate pain relief and fever reduction.', 3.00, 2000, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Ibuprofen 400mg', 'NSAID for pain, fever, and inflammation including headache, toothache, and arthritis.', 5.50, 1500, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Diclofenac 50mg', 'NSAID for musculoskeletal pain, osteoarthritis, and post-operative pain management.', 6.00, 800, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Naproxen 500mg', 'Long-acting NSAID for arthritis, menstrual pain, and acute gout attacks.', 7.00, 600, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Tramadol 50mg', 'Opioid-like analgesic for moderate to severe pain unresponsive to non-opioid analgesics.', 11.00, 400, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Codeine 30mg', 'Opioid analgesic and antitussive for mild to moderate pain and suppression of dry cough.', 9.50, 300, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Meloxicam 15mg', 'Preferential COX-2 inhibitor for osteoarthritis and rheumatoid arthritis with reduced GI risk.', 8.00, 500, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Celecoxib 200mg', 'Selective COX-2 inhibitor for arthritis pain with reduced risk of GI bleeding.', 20.00, 300, 1),

-- Cardiovascular
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Amlodipine 5mg', 'Calcium channel blocker for hypertension and stable angina.', 10.00, 700, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Lisinopril 10mg', 'ACE inhibitor for hypertension, heart failure, and post-myocardial infarction management.', 9.00, 600, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Metoprolol 50mg', 'Beta-1 selective blocker for hypertension, angina, and heart failure.', 8.50, 700, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Atorvastatin 20mg', 'HMG-CoA reductase inhibitor for hypercholesterolaemia and prevention of cardiovascular events.', 14.00, 800, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Simvastatin 40mg', 'Statin for reducing LDL cholesterol and risk of cardiovascular events in at-risk patients.', 12.00, 600, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Losartan 50mg', 'ARB for hypertension and diabetic nephropathy; well-tolerated alternative to ACE inhibitors.', 11.00, 500, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Furosemide 40mg', 'Loop diuretic for oedema associated with heart failure, renal impairment, and hepatic disease.', 5.00, 600, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Warfarin 5mg', 'Vitamin K antagonist anticoagulant for prevention of thromboembolism and AF management.', 6.50, 400, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Aspirin 100mg', 'Low-dose antiplatelet for secondary prevention of myocardial infarction and stroke.', 2.50, 1500, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Clopidogrel 75mg', 'Antiplatelet agent for ACS and secondary prevention of atherothrombotic events.', 18.00, 400, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Digoxin 0.25mg', 'Cardiac glycoside for heart failure and atrial fibrillation rate control.', 7.00, 300, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Spironolactone 25mg', 'Potassium-sparing diuretic for heart failure, hypertension, and primary hyperaldosteronism.', 9.00, 400, 1),

-- Diabetes & Endocrine
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Metformin 500mg', 'Biguanide for type 2 diabetes; reduces hepatic glucose output and improves insulin sensitivity.', 6.00, 1000, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Glibenclamide 5mg', 'Sulphonylurea that stimulates insulin secretion for type 2 diabetes management.', 5.50, 600, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Insulin Glargine 100IU/mL', 'Long-acting basal insulin analogue for type 1 and type 2 diabetes with once-daily dosing.', 85.00, 200, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Insulin Regular 100IU/mL', 'Short-acting insulin for prandial glucose control and management of diabetic ketoacidosis.', 60.00, 250, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Sitagliptin 100mg', 'DPP-4 inhibitor for type 2 diabetes with weight-neutral profile and low hypoglycaemia risk.', 45.00, 300, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Levothyroxine 50mcg', 'Synthetic T4 for hypothyroidism replacement therapy and TSH suppression.', 8.00, 500, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Carbimazole 5mg', 'Antithyroid drug that inhibits thyroid hormone synthesis for hyperthyroidism management.', 9.00, 300, 1),

-- Respiratory
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Salbutamol 100mcg Inhaler', 'Short-acting beta-2 agonist bronchodilator for acute asthma and COPD symptom relief.', 25.00, 400, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Budesonide 200mcg Inhaler', 'Inhaled corticosteroid for maintenance therapy of asthma and COPD exacerbation prevention.', 35.00, 300, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Montelukast 10mg', 'Leukotriene receptor antagonist for asthma prophylaxis and seasonal allergic rhinitis.', 22.00, 400, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Prednisolone 5mg', 'Corticosteroid for asthma exacerbations, inflammatory conditions, and immunosuppression.', 5.00, 600, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Dextromethorphan 15mg', 'Antitussive for suppression of non-productive dry cough in upper respiratory infections.', 4.50, 700, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Cetirizine 10mg', 'Second-generation antihistamine for allergic rhinitis, urticaria, and hay fever.', 4.00, 1000, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Loratadine 10mg', 'Non-sedating antihistamine for allergic conditions with minimal CNS effects.', 3.50, 1200, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Fexofenadine 180mg', 'Non-sedating antihistamine for chronic idiopathic urticaria and seasonal allergic rhinitis.', 8.00, 500, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Theophylline 200mg', 'Xanthine bronchodilator for maintenance treatment of asthma and COPD.', 10.00, 350, 1),

-- Gastrointestinal
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Omeprazole 20mg', 'Proton pump inhibitor for peptic ulcer, GORD, and Helicobacter pylori eradication regimens.', 8.00, 1000, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Pantoprazole 40mg', 'Proton pump inhibitor for erosive oesophagitis, stress ulcer prophylaxis, and GORD.', 9.50, 800, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Ranitidine 150mg', 'H2 receptor antagonist for peptic ulcer disease and gastro-oesophageal reflux.', 6.00, 600, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Domperidone 10mg', 'Dopamine antagonist antiemetic and prokinetic for nausea, vomiting, and gastroparesis.', 5.50, 700, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Ondansetron 8mg', 'Selective 5-HT3 antagonist for chemotherapy-induced and post-operative nausea and vomiting.', 18.00, 400, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Metoclopramide 10mg', 'Dopamine antagonist antiemetic and prokinetic for nausea, vomiting, and delayed gastric emptying.', 4.00, 800, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Loperamide 2mg', 'Opioid-receptor agonist antidiarrhoeal for acute and chronic diarrhoea management.', 5.00, 700, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Bisacodyl 5mg', 'Stimulant laxative for constipation and bowel preparation before procedures.', 4.00, 600, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Lactulose 667mg/mL', 'Osmotic laxative for constipation and hepatic encephalopathy ammonia reduction.', 7.50, 400, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Hyoscine Butylbromide 10mg', 'Antispasmodic for abdominal cramps and irritable bowel syndrome.', 6.00, 500, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Activated Charcoal 250mg', 'Adsorbent for acute poisoning and drug overdose management in emergency settings.', 12.00, 200, 1),

-- Neurology & Psychiatry
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Amitriptyline 25mg', 'Tricyclic antidepressant for depression, neuropathic pain, and migraine prophylaxis.', 7.00, 400, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Fluoxetine 20mg', 'SSRI antidepressant for major depressive disorder, OCD, and panic disorder.', 12.00, 500, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Sertraline 50mg', 'SSRI for depression, PTSD, panic disorder, and social anxiety disorder.', 13.00, 450, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Diazepam 5mg', 'Benzodiazepine for anxiety disorders, acute alcohol withdrawal, and muscle spasm.', 6.00, 300, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Alprazolam 0.5mg', 'Benzodiazepine for generalised anxiety disorder and panic disorder.', 8.00, 300, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Haloperidol 5mg', 'Typical antipsychotic for schizophrenia, acute psychosis, and Tourette syndrome.', 9.00, 250, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Risperidone 2mg', 'Atypical antipsychotic for schizophrenia, bipolar disorder, and irritability in autism.', 22.00, 300, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Carbamazepine 200mg', 'Anticonvulsant for epilepsy, trigeminal neuralgia, and bipolar disorder mood stabilisation.', 11.00, 400, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Valproate 500mg', 'Anticonvulsant and mood stabiliser for epilepsy, bipolar disorder, and migraine prevention.', 13.00, 350, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Phenytoin 100mg', 'Sodium channel blocker anticonvulsant for generalised tonic-clonic and focal seizures.', 10.00, 300, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Levodopa/Carbidopa 250/25mg', 'Gold-standard dopamine replacement therapy for Parkinson disease symptom management.', 30.00, 200, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Sumatriptan 50mg', 'Selective serotonin agonist for acute migraine attacks with or without aura.', 28.00, 250, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Zolpidem 10mg', 'Non-benzodiazepine hypnotic for short-term management of insomnia.', 14.00, 300, 1),

-- Vitamins & Supplements
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Vitamin C 500mg', 'Ascorbic acid supplement for scurvy prevention, immune support, and antioxidant therapy.', 3.50, 2000, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Vitamin D3 1000IU', 'Cholecalciferol for vitamin D deficiency, osteoporosis prevention, and immune modulation.', 5.00, 1500, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Vitamin B12 1000mcg', 'Cyanocobalamin for B12 deficiency, pernicious anaemia, and peripheral neuropathy.', 4.50, 1000, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Folic Acid 5mg', 'B-vitamin for prevention of neural tube defects and treatment of folate deficiency anaemia.', 2.50, 1500, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Iron Sulfate 325mg', 'Ferrous sulfate for iron-deficiency anaemia and iron supplementation in pregnancy.', 4.00, 1200, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Calcium Carbonate 500mg', 'Calcium supplement for osteoporosis prevention, hypocalcaemia, and antacid use.', 3.50, 1000, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Zinc Sulfate 220mg', 'Zinc supplement for deficiency states, wound healing, and immune function support.', 4.00, 800, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Magnesium Oxide 400mg', 'Magnesium supplement for hypomagnesaemia, constipation, and muscle cramp prevention.', 5.00, 700, 1),

-- Dermatology
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Hydrocortisone Cream 1%', 'Mild topical corticosteroid for eczema, contact dermatitis, and inflammatory skin conditions.', 6.00, 500, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Clotrimazole Cream 1%', 'Topical antifungal for tinea, candidal intertrigo, and vulvovaginal candidiasis.', 7.50, 450, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Mupirocin Ointment 2%', 'Topical antibiotic for impetigo and superficial skin infections caused by Staphylococcus.', 12.00, 300, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Aciclovir Cream 5%', 'Topical antiviral for herpes labialis (cold sores) and herpes simplex skin infections.', 15.00, 250, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Betamethasone Cream 0.1%', 'Potent topical corticosteroid for severe eczema, psoriasis, and lichen planus.', 9.00, 350, 1),

-- Ophthalmology & ENT
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Ciprofloxacin Eye Drops 0.3%', 'Topical antibiotic eye drops for bacterial conjunctivitis and corneal ulcers.', 14.00, 300, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Timolol Eye Drops 0.5%', 'Beta-blocker eye drops for reduction of intraocular pressure in open-angle glaucoma.', 18.00, 200, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Artificial Tears 0.5%', 'Lubricating eye drops for dry eye syndrome and ocular surface disease.', 8.00, 500, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Otosporin Ear Drops', 'Combination antibiotic and corticosteroid ear drops for otitis externa.', 16.00, 250, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Oxymetazoline Nasal Spray', 'Alpha-adrenergic nasal decongestant for short-term relief of nasal congestion.', 7.00, 400, 1),

-- Musculoskeletal
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Allopurinol 300mg', 'Xanthine oxidase inhibitor for chronic gout, hyperuricaemia, and uric acid renal calculi.', 7.50, 500, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Colchicine 0.5mg', 'Anti-inflammatory for acute gout attacks and familial Mediterranean fever prophylaxis.', 16.00, 300, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Hydroxychloroquine 200mg', 'Antimalarial and DMARD for rheumatoid arthritis, lupus, and malaria prophylaxis.', 20.00, 300, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Methotrexate 2.5mg', 'DMARD for rheumatoid arthritis, psoriasis, and certain malignancies at low dose.', 25.00, 200, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Baclofen 10mg', 'Muscle relaxant and antispastic agent for spasticity in multiple sclerosis and spinal cord injury.', 10.00, 350, 1),

-- Antimalarials & Antiparasitics
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Artemether/Lumefantrine 20/120mg', 'Artemisinin-based combination therapy for uncomplicated Plasmodium falciparum malaria.', 35.00, 200, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Chloroquine 250mg', 'Antimalarial for chloroquine-sensitive malaria and amoebic liver abscess.', 8.00, 300, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Albendazole 400mg', 'Broad-spectrum anthelmintic for intestinal nematodes, hydatid disease, and neurocysticercosis.', 6.50, 400, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Ivermectin 6mg', 'Antiparasitic for onchocerciasis, strongyloidiasis, and scabies.', 12.00, 250, 1),

-- Emergency & IV
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Adrenaline 1mg/mL Injection', 'First-line treatment for anaphylaxis, cardiac arrest, and severe bronchospasm.', 45.00, 150, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Atropine 1mg/mL Injection', 'Anticholinergic for bradycardia, organophosphate poisoning, and anaesthetic premedication.', 20.00, 150, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Morphine 10mg/mL Injection', 'Opioid analgesic for severe acute pain, post-operative analgesia, and palliative care.', 30.00, 200, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Normal Saline 0.9% 500mL', 'Isotonic crystalloid for fluid resuscitation, drug dilution, and wound irrigation.', 5.00, 1000, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Dextrose 50% 50mL Injection', 'Concentrated glucose solution for hypoglycaemia correction in emergency settings.', 8.00, 400, 1),
(UUID_TO_BIN(UUID()), NOW(), NOW(), 'Hydrocortisone 100mg Injection', 'Injectable corticosteroid for anaphylaxis, severe asthma, and adrenal crisis management.', 18.00, 250, 1);
