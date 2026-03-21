-- Add full-text index on patient name and phone for medical record search
ALTER TABLE account ADD FULLTEXT INDEX ft_account_search (full_name, phone);

-- Add full-text index on medicine table for medicine search (you'll use this in Phase 5)
ALTER TABLE medicine ADD FULLTEXT INDEX ft_medicine_search (name, description);