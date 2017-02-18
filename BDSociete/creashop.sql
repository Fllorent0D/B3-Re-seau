drop user SHOP cascade;
create user SHOP identified by oracle default tablespace users temporary tablespace temp profile default account unlock;
alter user SHOP quota unlimited on users;
grant myrole to SHOP;



	select 'alter system kill session ''' || sid || ',' || serial# || ''';' from v$session where username = 'SHOP';