CREATE OR REPLACE FUNCTION fn_log_aluno_inserido()
RETURNS TRIGGER AS $$
BEGIN
    RAISE NOTICE 'Trigger ativada! Novo aluno inserido com ID: %', NEW.id_alunos;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tg_log_aluno_inserido
AFTER INSERT ON alunos
FOR EACH ROW
EXECUTE FUNCTION fn_log_aluno_inserido();

CREATE OR REPLACE FUNCTION fn_log_atualizacao_aluno()
RETURNS TRIGGER AS $$
BEGIN
    RAISE NOTICE 'O aluno % (ID: %) teve seus dados alterados!', OLD.nome, OLD.id_alunos;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tg_auditoria_aluno
BEFORE UPDATE ON alunos
FOR EACH ROW
EXECUTE FUNCTION fn_log_atualizacao_aluno();

CREATE OR REPLACE FUNCTION fn_valida_data_matricula()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.data_matricula > CURRENT_DATE THEN
        RAISE EXCEPTION 'Erro: A data de matricula nao pode ser maior que hoje!';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tg_valida_data
BEFORE INSERT OR UPDATE ON alunos
FOR EACH ROW
EXECUTE FUNCTION fn_valida_data_matricula();
