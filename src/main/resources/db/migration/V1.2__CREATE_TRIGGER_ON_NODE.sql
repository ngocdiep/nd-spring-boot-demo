BEGIN TRANSACTION;

    CREATE INDEX node_parent_path_idx ON node USING GIST (parent_path);
    CREATE INDEX node_parent_id_idx ON node (parent_id);

    CREATE OR REPLACE FUNCTION update_node_parent_path() RETURNS TRIGGER AS $$
        DECLARE
            path ltree;
        BEGIN
            IF NEW.parent_id IS NULL THEN
                NEW.parent_path = ''::ltree;
            ELSEIF TG_OP = 'INSERT' OR OLD.parent_id IS NULL OR OLD.parent_id != NEW.parent_id THEN
                SELECT parent_path || id::text FROM node WHERE id = NEW.parent_id INTO path;
                IF path IS NULL THEN
                    RAISE EXCEPTION 'Invalid parent_id %', NEW.parent_id;
                END IF;
                NEW.parent_path = path;
                IF TG_OP = 'UPDATE' AND OLD.parent_id != NEW.parent_id THEN
                    UPDATE node SET parent_path = NEW.parent_path || subpath(parent_path, nlevel(OLD.parent_path))
                        WHERE parent_path <@ OLD.parent_path AND parent_path <> OLD.parent_path;
                END IF;        
            END IF;
            RETURN NEW;
        END;
    $$ LANGUAGE plpgsql;


    CREATE TRIGGER parent_path_tgr
        BEFORE INSERT OR UPDATE ON node
        FOR EACH ROW
        EXECUTE PROCEDURE update_node_parent_path();

END TRANSACTION;