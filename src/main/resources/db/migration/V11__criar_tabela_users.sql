create table users (
    id_user serial primary key,
    senha varchar(255) not null,
    tipo varchar(20) not null,
    criado_em date not null default current_date,
    status varchar(20) not null default 'ATIVO',
    id_aluno int unique,
    id_personal int unique,
    foreign key (id_aluno) references alunos (id_alunos),
    foreign key (id_personal) references personais (id_personal)
);
