delete from agendamentos where id_disponibilidade in (select id_disponibilidade from disponibilidades);
delete from disponibilidades;
alter table disponibilidades add column data date not null default current_date;
alter table disponibilidades alter column data drop default;
alter table disponibilidades drop column dia_semana;
