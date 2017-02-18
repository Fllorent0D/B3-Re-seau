drop table intervenants;
drop table activites;

create table intervenants
(
  id integer,
  role varchar(55),
  nom varchar(55),
  constraint intervenants$pk primary key(id),
  constraint role$check check (role in ('etudiant', 'prof'))
);

create table activites
(
  id integer,
  cours varchar(55),
  description varchar(255),
  gerant integer,
  constraint activites$pk primary key(id),
  constraint cours$check check (cours in('cours', 'cours-labo', 'travail-labo')),
  constraint gerant$fk$intervenant foreign key (gerant) references intervenants(id)
);

create or replace trigger activityGerant
before insert or update on activites
for each row
declare
  intervenant intervenants%rowtype;
  gerant_invalide exception;
begin
  select * into intervenant from intervenants where id = :new.gerant;

  if( ((:new.cours = 'cours' OR :new.cours = 'cours-labo') AND :new.gerant = 'etudiant') OR (:new.cours = 'travail-labo' AND :new.gerant = 'prof') )
    then raise gerant_invalide;
  end if;
  
exception
  when gerant_invalide then raise_application_error(-20001, 'Type de gérant invalide pour un cours du type ' || :new.cours);
  when others then raise;
end;

insert into intervenants values(1, 'prof', 'Vilvens');
insert into intervenants values(2, 'prof', 'Herbiet');
insert into intervenants values(3, 'etudiant', 'Bastin');
insert into intervenants values(4, 'etudiant', 'Cardoen');

insert into activites values(1, 'cours', 'théorie', 1);
insert into activites values(2, 'cours-labo', 'mise en pratique', 2);
insert into activites values(3, 'travail-labo', 'projet', 3);

-- bad insert
insert into activites values(4, 'travail-labo', 'projet', 1);