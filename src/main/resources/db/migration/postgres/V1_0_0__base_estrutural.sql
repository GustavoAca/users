do $$
  declare
    r record;
    foo varchar(50);
  begin
    for r in
      SELECT tableOWNER, TABLENAME FROM pg_tables WHERE tableOWNER='att_user' and tablename <> 'FLYWAY_SCHEMA_HISTORY'
    loop
      EXECUTE 'GRANT SELECT, UPDATE, INSERT, DELETE ON '||r.tableOWNER||'.'||r.TABLENAME||' TO att_user_app';
    end loop;
  end;
$$;