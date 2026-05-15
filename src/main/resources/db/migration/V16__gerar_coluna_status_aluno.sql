ALTER TABLE alunos
ADD COLUMN status VARCHAR(255);
UPDATE alunos SET status = 'ATIVO';