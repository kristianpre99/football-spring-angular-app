SELECT 'CREATE DATABASE football_db_2'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'football_db_2')