
CREATE OR REPLACE FUNCTION "PRUEBA"."FU_MODULO11"
(
	P_RUT_SIN_DV IN INT,
	P_RUT_DV IN INT
)
RETURN BOOLEAN IS valido BOOLEAN;
    l_resto int := P_RUT_SIN_DV;
    l_suma int := 0;
    l_multiplicador int := 2;
    l_rut_dv int;
BEGIN 

	IF P_RUT_DV = 0 THEN
		l_rut_dv:=11;
    ELSE
        l_rut_dv:=P_RUT_DV;
	END IF;
    WHILE TRUE 
    LOOP
        l_suma := l_suma + l_multiplicador*MOD(l_resto,10);
        l_resto := TRUNC(l_resto/10);
        IF l_resto = 0 then
            EXIT;
        end if;
        IF l_multiplicador = 7 THEN
            l_multiplicador := 2;
        ELSE
            l_multiplicador := l_multiplicador+1;
        END IF;
    END LOOP;
    
    IF l_rut_dv = (11 - MOD(l_suma,11)) THEN
        valido := TRUE;
    ELSE
        valido := FALSE;
    END IF;
	RETURN valido;
END;


CREATE OR REPLACE PROCEDURE "PRUEBA"."PR_CREAR_USUARIO"
(
	P_RETURN_VALUE OUT INT,
	P_NOMBRE_USUARIO IN varchar2,
	P_CLAVE IN varchar2,
	P_TIPO_USUARIO IN varchar2,
	P_FUNCIONARIO_RUN_SIN_DV IN int
) AS
	l_existe INT;
	e_id_funcionario_no_existe EXCEPTION;
	e_nombre_usuario_existe EXCEPTION;
BEGIN
	SELECT COUNT(*) INTO l_existe FROM FUNCIONARIO WHERE run_sin_dv = P_FUNCIONARIO_RUN_SIN_DV;
	IF l_existe = 0 THEN
		RAISE e_id_funcionario_no_existe;
	END IF;
	SELECT COUNT(*) INTO l_existe FROM USUARIO WHERE nombre_usuario = P_NOMBRE_USUARIO;
	IF l_existe > 0 THEN
		RAISE e_nombre_usuario_existe;
	END IF;
	INSERT INTO USUARIO(
		id_usuario,
		nombre_usuario,
		clave,
		tipo_usuario,
		funcionario_run_sin_dv) 
	VALUES(
		USUARIO_SEQ.NEXTVAL,
		P_NOMBRE_USUARIO,
		P_CLAVE,
		P_TIPO_USUARIO,
		P_FUNCIONARIO_RUN_SIN_DV
	);
	P_RETURN_VALUE := 0;
EXCEPTION
	WHEN e_id_funcionario_no_existe THEN
    P_RETURN_VALUE := 100101;
	WHEN e_nombre_usuario_existe THEN
    P_RETURN_VALUE := 100102;
END PR_CREAR_USUARIO;



CREATE OR REPLACE PROCEDURE "PRUEBA"."PR_MODIFICAR_USUARIO"
(
	P_RETURN_VALUE OUT INT,
	P_ID_USUARIO IN INT,
	P_NOMBRE_USUARIO IN varchar2,
	P_CLAVE IN varchar2,
	P_TIPO_USUARIO IN varchar2,
	P_FUNCIONARIO_RUN_SIN_DV IN int
) AS
	CURSOR c_usuario_modificar IS 
		SELECT * FROM USUARIO;
    rec_usuario USUARIO%ROWTYPE;
	l_usuario_existe BOOLEAN;
	l_existe INT;
	e_id_funcionario_no_existe EXCEPTION;
	e_nombre_usuario_existe EXCEPTION;
	e_usuario_no_existe EXCEPTION;
BEGIN
	l_usuario_existe := FALSE;
	OPEN c_usuario_modificar;
    LOOP
        FETCH c_usuario_modificar INTO rec_usuario;
		IF c_usuario_modificar%NOTFOUND THEN
			EXIT;
		END IF;
        IF rec_usuario.id_usuario = P_ID_USUARIO THEN
            l_usuario_existe := TRUE;
		ELSE
			IF rec_usuario.nombre_usuario = P_NOMBRE_USUARIO THEN
				RAISE e_nombre_usuario_existe;
			END IF;
        END IF;
    END LOOP;
    CLOSE c_usuario_modificar;
	IF NOT l_usuario_existe THEN
		RAISE e_usuario_no_existe;
	END IF;
	SELECT COUNT(*) INTO l_existe FROM FUNCIONARIO WHERE run_sin_dv = P_FUNCIONARIO_RUN_SIN_DV;
	IF l_existe = 0 THEN
		RAISE e_id_funcionario_no_existe;
	END IF;
	
	UPDATE USUARIO
	SET 
		nombre_usuario = P_NOMBRE_USUARIO,
		clave = P_CLAVE,
		tipo_usuario = P_TIPO_USUARIO,
		funcionario_run_sin_dv = P_FUNCIONARIO_RUN_SIN_DV
	WHERE id_usuario = P_ID_USUARIO;
	P_RETURN_VALUE := 0;
EXCEPTION
	WHEN e_id_funcionario_no_existe THEN
    P_RETURN_VALUE := 100201;
	WHEN e_nombre_usuario_existe THEN
    P_RETURN_VALUE := 100202;
	WHEN e_usuario_no_existe THEN
    P_RETURN_VALUE := 100203;
END PR_MODIFICAR_USUARIO;



CREATE OR REPLACE PROCEDURE "PRUEBA"."PR_ELIMINAR_USUARIO"
(
	P_RETURN_VALUE OUT INT,
	P_ID_USUARIO IN varchar2
) AS
	l_existe INT;
	e_usuario_no_existe EXCEPTION;
BEGIN
	SELECT COUNT(*) INTO l_existe FROM USUARIO WHERE id_usuario = P_ID_USUARIO;
	IF l_existe = 0 THEN
		RAISE e_usuario_no_existe;
	END IF;
	
	DELETE USUARIO
	WHERE id_usuario = P_ID_USUARIO;
	P_RETURN_VALUE := 0;
EXCEPTION
	WHEN e_usuario_no_existe THEN
    P_RETURN_VALUE := 100301;
END PR_ELIMINAR_USUARIO;




CREATE OR REPLACE PROCEDURE "PRUEBA"."PR_CREAR_UNIDAD"
(
    P_RETURN_VALUE OUT int,
	P_NOMBRE_UNIDAD IN varchar2,
	P_DESCRIPCION_UNIDAD IN varchar2,
	P_DIRECCION_UNIDAD IN varchar2,
	P_UNIDAD_PADRE IN int  DEFAULT NULL,
	P_JEFE_UNIDAD IN int DEFAULT NULL
) AS
	l_existe INT;
	e_nombre_unidad_existe EXCEPTION;
	e_id_padre_no_existe EXCEPTION;
	e_id_jefe_no_existe EXCEPTION;
BEGIN
	SELECT COUNT(*) INTO l_existe FROM UNIDAD WHERE nombre_unidad = P_NOMBRE_UNIDAD;
	IF l_existe > 0 THEN
		RAISE e_nombre_unidad_existe;
	END IF;
	IF P_UNIDAD_PADRE IS NOT NULL THEN
		SELECT COUNT(*) INTO l_existe FROM UNIDAD WHERE id_unidad = P_UNIDAD_PADRE;
		IF l_existe = 0 THEN
			RAISE e_id_padre_no_existe;
		END IF;
	END IF;
	IF P_JEFE_UNIDAD IS NOT NULL THEN
		SELECT COUNT(*) INTO l_existe FROM FUNCIONARIO WHERE run_sin_dv = P_JEFE_UNIDAD;
		IF l_existe = 0 THEN
			RAISE e_id_jefe_no_existe;
		END IF;
	END IF;
	INSERT INTO UNIDAD(
		id_unidad,
		nombre_unidad,
		descripcion_unidad,
		direccion_unidad,
		habilitado,
		unidad_padre_id_unidad,
		jefe_unidad_run_sin_dv) 
	VALUES(
		UNIDAD_SEQ.NEXTVAL,
		P_NOMBRE_UNIDAD,
		P_DESCRIPCION_UNIDAD,
		P_DIRECCION_UNIDAD,
		1, -- Todas las unidades se encuentran habilitadas al crearse.
		P_UNIDAD_PADRE,
		P_JEFE_UNIDAD
	);
    P_RETURN_VALUE := 0;
EXCEPTION
	WHEN e_nombre_unidad_existe THEN
    P_RETURN_VALUE := 110101;
	WHEN e_id_padre_no_existe THEN
    P_RETURN_VALUE := 110102;
	WHEN e_id_jefe_no_existe THEN
    P_RETURN_VALUE := 110103;
END PR_CREAR_UNIDAD;



CREATE OR REPLACE PROCEDURE "PRUEBA"."PR_MODIFICAR_UNIDAD"
(
    P_RETURN_VALUE OUT int,
	P_ID_UNIDAD IN int,
	P_NOMBRE_UNIDAD IN varchar2,
	P_DESCRIPCION_UNIDAD IN varchar2,
	P_DIRECCION_UNIDAD IN varchar2,
	P_HABILITADO IN int,
	P_UNIDAD_PADRE IN int  DEFAULT NULL,
	P_JEFE_UNIDAD IN int DEFAULT NULL
) AS
	CURSOR c_unidad_modificar IS 
		SELECT * FROM UNIDAD;
    rec_unidad UNIDAD%ROWTYPE;
	l_unidad_existe BOOLEAN;
	l_padre_existe BOOLEAN;
	l_existe INT;
	e_nombre_unidad_existe EXCEPTION;	--El cambio de nombre de la unidad ya pertenece a otra unidad.
	e_id_padre_no_existe EXCEPTION;		--El padre de la unidad (declarado) no existe.
	e_id_jefe_no_existe EXCEPTION;		--El funcionario jefe de la unidad (declarado) no existe.
	e_unidad_no_existe EXCEPTION;		--La unidad a modificar no existe.
	e_padre_es_identidad EXCEPTION;		--Cuando la unidad padre es la propia identidad a modificar.
BEGIN
	IF P_ID_UNIDAD = P_UNIDAD_PADRE THEN
		RAISE e_padre_es_identidad;
	END IF;
	l_unidad_existe := FALSE;
	IF P_UNIDAD_PADRE IS NOT NULL THEN
		l_padre_existe := FALSE;
	ELSE
		l_padre_existe := TRUE;
	END IF;
	OPEN c_unidad_modificar;
    LOOP
        FETCH c_unidad_modificar INTO rec_unidad;
		IF c_unidad_modificar%NOTFOUND THEN
			EXIT;
		END IF;
        IF rec_unidad.id_unidad = P_ID_UNIDAD THEN
            l_unidad_existe := TRUE;
		ELSE
			IF rec_unidad.unidad_padre_id_unidad = P_UNIDAD_PADRE THEN
				l_padre_existe := TRUE;
			END IF;
			IF rec_unidad.nombre_unidad = P_NOMBRE_UNIDAD THEN
				RAISE e_nombre_unidad_existe;
			END IF;
        END IF;
    END LOOP;
    CLOSE c_unidad_modificar;
	IF NOT l_unidad_existe THEN
		RAISE e_unidad_no_existe;
	END IF;
	IF NOT l_padre_existe THEN
		RAISE e_id_padre_no_existe;
	END IF;
	IF P_JEFE_UNIDAD IS NOT NULL THEN
		SELECT COUNT(*) INTO l_existe FROM FUNCIONARIO WHERE run_sin_dv = P_JEFE_UNIDAD;
		IF l_existe = 0 THEN
			RAISE e_id_jefe_no_existe;
		END IF;
	END IF;
	
	UPDATE UNIDAD
	SET
		nombre_unidad = P_NOMBRE_UNIDAD,
		descripcion_unidad = P_DESCRIPCION_UNIDAD,
		direccion_unidad = P_DIRECCION_UNIDAD,
		habilitado = P_HABILITADO,
		unidad_padre_id_unidad = P_UNIDAD_PADRE,
		jefe_unidad_run_sin_dv = P_JEFE_UNIDAD
	WHERE
		id_unidad = P_ID_UNIDAD;
    P_RETURN_VALUE := 0;
EXCEPTION
	WHEN e_nombre_unidad_existe THEN
    P_RETURN_VALUE := 110201;
	WHEN e_id_padre_no_existe THEN
    P_RETURN_VALUE := 110202;
	WHEN e_id_jefe_no_existe THEN
    P_RETURN_VALUE := 110203;
	WHEN e_unidad_no_existe THEN
    P_RETURN_VALUE := 110204;
	WHEN e_padre_es_identidad THEN
    P_RETURN_VALUE := 110205;
END PR_MODIFICAR_UNIDAD;



CREATE OR REPLACE PROCEDURE "PRUEBA"."PR_ELIMINAR_UNIDAD"
(
    P_RETURN_VALUE OUT int,
	P_ID_UNIDAD IN int
) AS
	l_existe INT;
	e_unidad_no_existe EXCEPTION;		--La unidad a eliminar no existe.
BEGIN
	SELECT COUNT(*) INTO l_existe FROM UNIDAD WHERE id_unidad = P_ID_UNIDAD;
	IF l_existe = 0 THEN
		RAISE e_unidad_no_existe;
	END IF;
	
	
	UPDATE UNIDAD
	SET
		habilitado = 0
	WHERE id_unidad = P_ID_UNIDAD;
    P_RETURN_VALUE := 0;
EXCEPTION
	WHEN e_unidad_no_existe THEN
    P_RETURN_VALUE := 110304;
END PR_ELIMINAR_UNIDAD;



CREATE OR REPLACE PROCEDURE "PRUEBA"."PR_CREAR_FUNCIONARIO"
(
	P_RETURN_VALUE OUT int,
	P_RUN_SIN_DV IN int,
	P_RUN_DV IN int,
	P_NOM_FUNCIONARIO IN varchar2,
	P_AP_PATERNO IN varchar2,
	P_AP_MATERNO IN varchar2,
	P_FEC_NACIMIENTO IN date,
	P_CORREO IN varchar2,
	P_DIREC_FUNCIONARIO IN varchar2,
	P_CARGO IN varchar2,
	P_UNIDAD_ID_UNIDAD IN int DEFAULT NULL
) AS
	l_existe INT;
	e_id_unidad_no_existe EXCEPTION;
	e_dv_invalido EXCEPTION;
	e_rut_existe EXCEPTION;
BEGIN
	IF P_UNIDAD_ID_UNIDAD IS NOT NULL THEN
		SELECT COUNT(*) INTO l_existe FROM UNIDAD WHERE id_unidad = P_UNIDAD_ID_UNIDAD;
		IF l_existe = 0 THEN
			RAISE e_id_unidad_no_existe;
		END IF;
	END IF;
	SELECT COUNT(*) INTO l_existe FROM FUNCIONARIO WHERE run_sin_dv = P_RUN_SIN_DV;
	IF l_existe > 0 THEN
		RAISE e_rut_existe;
	END IF;
	IF NOT FU_MODULO11(P_RUN_SIN_DV,P_RUN_DV) THEN
		RAISE e_dv_invalido;
	END IF;
	
	INSERT INTO FUNCIONARIO(
		run_sin_dv,
		run_dv,
		nom_funcionario,
		ap_paterno,
		ap_materno,
		fec_nacimiento,
		correo,
		direc_funcionario,
		cargo,
		habilitado,
		unidad_id_unidad) 
	VALUES(
		P_RUN_SIN_DV,
		P_RUN_DV,
		P_NOM_FUNCIONARIO,
		P_AP_PATERNO,
		P_AP_MATERNO,
		P_FEC_NACIMIENTO,
		P_CORREO,
		P_DIREC_FUNCIONARIO,
		P_CARGO,
		1,  --Todos los funcionarios se encuentran habilitado al crearse
		P_UNIDAD_ID_UNIDAD);
	P_RETURN_VALUE := 0;
EXCEPTION
	WHEN e_id_unidad_no_existe THEN
    P_RETURN_VALUE := 120101;
	WHEN e_dv_invalido THEN
    P_RETURN_VALUE := 120102;
	WHEN e_rut_existe THEN
    P_RETURN_VALUE := 120103;
END PR_CREAR_FUNCIONARIO;



create or replace PROCEDURE "PRUEBA"."PR_MODIFICAR_FUNCIONARIO"
(
	P_RETURN_VALUE OUT int,
	P_RUN_SIN_DV IN int,
	--P_RUN_DV IN int,
	P_NOM_FUNCIONARIO IN varchar2,
	P_AP_PATERNO IN varchar2,
	P_AP_MATERNO IN varchar2,
	P_FEC_NACIMIENTO IN date,
	P_CORREO IN varchar2,
	P_DIREC_FUNCIONARIO IN varchar2,
	P_CARGO IN varchar2,
	P_HABILITADO IN int,
	P_UNIDAD_ID_UNIDAD IN int DEFAULT NULL
) AS
	CURSOR c_funcionario_modificar IS
		SELECT * FROM FUNCIONARIO;
    rec_funcionario FUNCIONARIO%ROWTYPE;
	l_funcionario_existe BOOLEAN;
	l_existe INT;
	e_id_unidad_no_existe EXCEPTION;
	--e_dv_invalido EXCEPTION;
	e_funcionario_no_existe EXCEPTION;
	e_correo_existe EXCEPTION;
BEGIN
	l_funcionario_existe := FALSE;
	OPEN c_funcionario_modificar;
    LOOP
        FETCH c_funcionario_modificar INTO rec_funcionario;
		IF c_funcionario_modificar%NOTFOUND THEN
			EXIT;
		END IF;
        IF rec_funcionario.run_sin_dv = P_RUN_SIN_DV THEN   --Se verifica que el funcionario a modificar existe, por defecto esta conciderado que no existe hasta que lo encuentre.
            l_funcionario_existe := TRUE;
		ELSE
			IF rec_funcionario.correo = P_CORREO THEN  -- No se permite ingresar un correo perteneciente a otro funcionario.
				RAISE e_correo_existe;
			END IF;
        END IF;
    END LOOP;
    CLOSE c_funcionario_modificar;
	IF NOT l_funcionario_existe THEN
		RAISE e_funcionario_no_existe;
	END IF;
	IF P_UNIDAD_ID_UNIDAD IS NOT NULL THEN
		SELECT COUNT(*) INTO l_existe FROM UNIDAD WHERE id_unidad = P_UNIDAD_ID_UNIDAD;
		IF l_existe = 0 THEN
			RAISE e_id_unidad_no_existe;
		END IF;
	END IF;
	/*
	IF NOT FU_MODULO11(P_RUN_SIN_DV,P_RUN_DV) THEN
		RAISE e_dv_invalido;
	END IF;
	*/

	UPDATE FUNCIONARIO
	SET
		--run_dv = P_RUN_DV,
		nom_funcionario = P_NOM_FUNCIONARIO,
		ap_paterno = P_AP_PATERNO,
		ap_materno = P_AP_MATERNO,
		fec_nacimiento = P_FEC_NACIMIENTO,
		correo = P_CORREO,
		direc_funcionario = P_DIREC_FUNCIONARIO,
		cargo = P_CARGO,
		habilitado = P_HABILITADO,
		unidad_id_unidad = P_UNIDAD_ID_UNIDAD
	WHERE run_sin_dv = P_RUN_SIN_DV;
	P_RETURN_VALUE := 0;
EXCEPTION
	WHEN e_id_unidad_no_existe THEN
    P_RETURN_VALUE := 120201;
	WHEN e_funcionario_no_existe THEN
    P_RETURN_VALUE := 120202;
	WHEN e_correo_existe THEN  --Caso que el correo que se desea modificar ya le pertenece a un funcionario.
    P_RETURN_VALUE := 120203;
	/*WHEN e_dv_invalido THEN
    P_RETURN_VALUE := 120202;
	WHEN OTHERS THEN
	DECLARE codigo_error NUMBER := SQLCODE;
    BEGIN
        IF codigo_error = -2292 THEN --Caso de que se trate de modificar el run de un funcionario que es jefe unidad o tiene permisos permisos/resoluciones asociadas a el.
            P_RETURN_VALUE := 2292;
        END IF;
    END;
	*/
END PR_MODIFICAR_FUNCIONARIO;



create or replace PROCEDURE "PRUEBA"."PR_ELIMINAR_FUNCIONARIO"
(
	P_RETURN_VALUE OUT int,
	P_RUN_SIN_DV IN int
) AS
	l_existe INT;
	e_funcionario_no_existe EXCEPTION;
BEGIN
	SELECT COUNT(*) INTO l_existe FROM FUNCIONARIO WHERE run_sin_dv = P_RUN_SIN_DV;
	IF l_existe = 0 THEN
		RAISE e_funcionario_no_existe;
	END IF;
	
	UPDATE FUNCIONARIO
	SET
		habilitado = 0
	WHERE run_sin_dv = P_RUN_SIN_DV;
	P_RETURN_VALUE := 0;
EXCEPTION
	WHEN e_funcionario_no_existe THEN
    P_RETURN_VALUE := 120301;
END PR_ELIMINAR_FUNCIONARIO;





create or replace PROCEDURE          "PR_CREAR_PERMISO"
(
	P_RETURN_VALUE OUT int,
	P_TIPO_PERMISO IN varchar2,
	P_FECHA_INICIO IN date,
	P_FECHA_TERMINO IN date,
	P_DESC_PERMISO IN varchar2,
	P_SOLICITANTE_RUN IN int
) AS
	l_existe INT;
	e_fechas_invalidas EXCEPTION;
	e_rut_no_existe EXCEPTION;
BEGIN
	IF TRUNC(P_FECHA_INICIO) > TRUNC(P_FECHA_TERMINO) OR TRUNC(P_FECHA_INICIO) >= TRUNC(SYSDATE) THEN
		RAISE e_fechas_invalidas;
	END IF;
	SELECT COUNT(*) INTO l_existe FROM FUNCIONARIO WHERE run_sin_dv = P_SOLICITANTE_RUN;
	IF l_existe = 0 THEN
		RAISE e_rut_no_existe;
	END IF;


	INSERT INTO SOL_PERMISO(
		id_permiso,
		tipo_permiso,
		estado,
		fecha_inicio,
		fecha_termino,
		fecha_solicitud,
		desc_permiso,
		solicitante_run_sin_dv,
		autorizante_run_sin_dv) 
	VALUES(
		SOL_PERMISOS_SEQ.NEXTVAL,
		P_TIPO_PERMISO,
		NULL,
		TRUNC(P_FECHA_INICIO),
		TRUNC(P_FECHA_TERMINO),
		SYSDATE,
		P_DESC_PERMISO,
		P_SOLICITANTE_RUN,
		NULL);
	P_RETURN_VALUE := 0;
EXCEPTION
	WHEN e_fechas_invalidas THEN
    P_RETURN_VALUE := 130101;
	WHEN e_rut_no_existe THEN
    P_RETURN_VALUE := 130102;
END PR_CREAR_PERMISO;



CREATE OR REPLACE PROCEDURE "PRUEBA"."PR_AUTORIZAR_PERMISO"
(
	P_RETURN_VALUE OUT int,
	P_ID_PERMISO IN int,
	P_AUTORIZANTE_RUN IN int
) AS
	l_existe INT;
	e_permiso_no_existe EXCEPTION;
	e_autorizante_no_es_valido EXCEPTION;
BEGIN
	
	SELECT COUNT(*) INTO l_existe FROM SOL_PERMISO WHERE id_permiso = P_ID_PERMISO;
	IF l_existe = 0 THEN
		RAISE e_permiso_no_existe;
	END IF;
	
	SELECT COUNT(*)
	INTO l_existe
	FROM SOL_PERMISO p
	INNER JOIN FUNCIONARIO f ON p.solicitante_run_sin_dv = f.run_sin_dv
	INNER JOIN UNIDAD u ON f.unidad_id_unidad = u.id_unidad
	WHERE p.id_permiso = P_ID_PERMISO AND u.jefe_unidad_run_sin_dv = P_AUTORIZANTE_RUN;
	IF l_existe = 0 THEN
		RAISE e_autorizante_no_es_valido;
	END IF;
	
	UPDATE SOL_PERMISO
	SET
		estado = 1,
		autorizante_run_sin_dv = P_AUTORIZANTE_RUN
	WHERE id_permiso = P_ID_PERMISO;
	P_RETURN_VALUE := 0;
EXCEPTION
	WHEN e_permiso_no_existe THEN
    P_RETURN_VALUE := 130201;
	WHEN e_autorizante_no_es_valido THEN
    P_RETURN_VALUE := 130202;
END PR_AUTORIZAR_PERMISO;



CREATE OR REPLACE PROCEDURE "PRUEBA"."PR_RECHAZAR_PERMISO"
(
	P_RETURN_VALUE OUT int,
	P_ID_PERMISO IN int,
	P_AUTORIZANTE_RUN IN int
) AS
	l_existe INT;
	e_permiso_no_existe EXCEPTION;
	e_autorizante_no_es_valido EXCEPTION;
BEGIN
	
	SELECT COUNT(*) INTO l_existe FROM SOL_PERMISO WHERE id_permiso = P_ID_PERMISO;
	IF l_existe = 0 THEN
		RAISE e_permiso_no_existe;
	END IF;
	
	SELECT COUNT(*)
	INTO l_existe
	FROM SOL_PERMISO p
	INNER JOIN FUNCIONARIO f ON p.solicitante_run_sin_dv = f.run_sin_dv
	INNER JOIN UNIDAD u ON f.unidad_id_unidad = u.id_unidad
	WHERE p.id_permiso = P_ID_PERMISO AND u.jefe_unidad_run_sin_dv = P_AUTORIZANTE_RUN;
	IF l_existe = 0 THEN
		RAISE e_autorizante_no_es_valido;
	END IF;
	
	UPDATE SOL_PERMISO
	SET
		estado = 0,
		autorizante_run_sin_dv = P_AUTORIZANTE_RUN
	WHERE id_permiso = P_ID_PERMISO;
	P_RETURN_VALUE := 0;
EXCEPTION
	WHEN e_permiso_no_existe THEN
    P_RETURN_VALUE := 130301;
	WHEN e_autorizante_no_es_valido THEN
    P_RETURN_VALUE := 130302;
END PR_RECHAZAR_PERMISO;

CREATE OR REPLACE PROCEDURE "PRUEBA"."PR_VALIDAR_RESOLUCION"
(
	P_RETURN_VALUE OUT int,
	P_ID_RESOLUCION IN int,
	P_RESOLVENTE_RUN IN int
) AS
	l_existe INT;
	e_resolucion_no_existe EXCEPTION;
	e_resolvente_no_es_valido EXCEPTION;
BEGIN
	
	SELECT COUNT(*) INTO l_existe FROM RESOLUCION WHERE id_resolucion = P_ID_RESOLUCION;
	IF l_existe = 0 THEN
		RAISE e_resolucion_no_existe;
	END IF;
	
	SELECT COUNT(*)
	INTO l_existe
	FROM RESOLUCION r
	INNER JOIN SOL_PERMISO p ON r.permiso_id_permiso = p.id_permiso
	INNER JOIN FUNCIONARIO f ON p.solicitante_run_sin_dv = f.run_sin_dv
	INNER JOIN UNIDAD u ON f.unidad_id_unidad = u.id_unidad
	LEFT JOIN UNIDAD pu ON u.unidad_padre_id_unidad = pu.id_unidad
	LEFT JOIN UNIDAD ppu ON pu.unidad_padre_id_unidad = ppu.id_unidad
	WHERE r.id_resolucion = P_ID_RESOLUCION AND (u.jefe_unidad_run_sin_dv = P_RESOLVENTE_RUN OR ppu.jefe_unidad_run_sin_dv = P_RESOLVENTE_RUN OR ppu.jefe_unidad_run_sin_dv = P_RESOLVENTE_RUN);
	IF l_existe = 0 THEN
		RAISE e_resolvente_no_es_valido;
	END IF;
	
	UPDATE RESOLUCION
	SET
		estado_resolucion = 1,
		resolvente_run_sin_dv = P_RESOLVENTE_RUN
	WHERE id_resolucion = P_ID_RESOLUCION;
	P_RETURN_VALUE := 0;
EXCEPTION
	WHEN e_resolucion_no_existe THEN
    P_RETURN_VALUE := 140101;
	WHEN e_resolvente_no_es_valido THEN
    P_RETURN_VALUE := 140102;
END PR_VALIDAR_RESOLUCION;


CREATE OR REPLACE PROCEDURE "PRUEBA"."PR_INVALIDAR_RESOLUCION"
(
	P_RETURN_VALUE OUT int,
	P_ID_RESOLUCION IN int,
	P_RESOLVENTE_RUN IN int
) AS
	l_existe INT;
	e_resolucion_no_existe EXCEPTION;
	e_resolvente_no_es_valido EXCEPTION;
BEGIN
	
	SELECT COUNT(*) INTO l_existe FROM RESOLUCION WHERE id_resolucion = P_ID_RESOLUCION;
	IF l_existe = 0 THEN
		RAISE e_resolucion_no_existe;
	END IF;
	
	SELECT COUNT(*)
	INTO l_existe
	FROM RESOLUCION r
	INNER JOIN SOL_PERMISO p ON r.permiso_id_permiso = p.id_permiso
	INNER JOIN FUNCIONARIO f ON p.solicitante_run_sin_dv = f.run_sin_dv
	INNER JOIN UNIDAD u ON f.unidad_id_unidad = u.id_unidad
	LEFT JOIN UNIDAD pu ON u.unidad_padre_id_unidad = pu.id_unidad
	LEFT JOIN UNIDAD ppu ON pu.unidad_padre_id_unidad = ppu.id_unidad
	WHERE r.id_resolucion = P_ID_RESOLUCION AND (u.jefe_unidad_run_sin_dv = P_RESOLVENTE_RUN OR ppu.jefe_unidad_run_sin_dv = P_RESOLVENTE_RUN OR ppu.jefe_unidad_run_sin_dv = P_RESOLVENTE_RUN);
	IF l_existe = 0 THEN
		RAISE e_resolvente_no_es_valido;
	END IF;
	
	UPDATE RESOLUCION
	SET
		estado_resolucion = 0,
		resolvente_run_sin_dv = P_RESOLVENTE_RUN
	WHERE id_resolucion = P_ID_RESOLUCION;
	P_RETURN_VALUE := 0;
EXCEPTION
	WHEN e_resolucion_no_existe THEN
    P_RETURN_VALUE := 140201;
	WHEN e_resolvente_no_es_valido THEN
    P_RETURN_VALUE := 140202;
END PR_INVALIDAR_RESOLUCION;


CREATE OR REPLACE PROCEDURE "PRUEBA"."PR_RECAPITULACION_PERMISOS"
AS
CURSOR c_permisos IS 
    select p.id_permiso, r.id_resolucion
    from sol_permiso p
    left join resolucion r on p.id_permiso = r.permiso_id_permiso
    where trunc(p.fecha_termino) between add_months(trunc(sysdate,'mm'),-1) and last_day(add_months(trunc(sysdate,'mm'),-1))
    and p.estado = 1;
rec_permiso c_permisos%ROWTYPE;
begin
    IF TRUNC(SYSDATE) = trunc(trunc(sysdate,'mm') + 6,'iw') THEN
        OPEN c_permisos;
        LOOP
            FETCH c_permisos INTO rec_permiso;
            IF c_permisos%NOTFOUND THEN
                EXIT;
            END IF;
            IF rec_permiso.id_resolucion is null THEN
                INSERT INTO resolucion(
                id_resolucion,
                fec_resolucion,
                estado_resolucion,
                permiso_id_permiso,
                resolvente_run_sin_dv) 
                VALUES(
                    RESOLUCION_SEQ.NEXTVAL,
                    SYSDATE,
                    NULL,
                    rec_permiso.id_permiso,
                    NULL);
                --DBMS_OUTPUT.PUT_LINE (rec_permiso.id_permiso);
            END IF;
        END LOOP;
    END IF;
end PR_RECAPITULACION_PERMISOS;



-------------SCHEDULERS, JOBS, PROGRAMS ------------------------------------------
EXECUTE SYS.dbms_scheduler.drop_schedule('"INTERVALO_CADA_LUNES_0600"');

begin
dbms_scheduler.create_schedule
(schedule_name => 'INTERVALO_CADA_LUNES_0600',
 start_date=> trunc(sysdate)+6/24,
 repeat_interval=> 'FREQ=DAILY; BYDAY=MON; BYHOUR=6;',
 comments=>'Ejecuccion: Cada lunes a las 6');
end;


begin
-- Call a procedure of a database package
dbms_scheduler.create_program
(program_name=> 'PROG_RECAPITULACION_PERMISOS',
 program_type=> 'STORED_PROCEDURE',
 program_action=> 'PRUEBA.PR_RECAPITULACION_PERMISOS',
 enabled=>true,
 comments=>'Procedure para generar las resoluciiones pendientes del mes anterior'
 );
end;

begin
-- Connect both dbms_scheduler parts by creating the final job
dbms_scheduler.create_job
 (job_name => 'JOB_RECAPITULACION_PERMISOS',
  program_name=> 'PROG_RECAPITULACION_PERMISOS',
  schedule_name=>'INTERVALO_CADA_LUNES_0600',
  enabled=>true,
  auto_drop=>false,
  comments=>'Job para generar las resoluciones pendientes del mes anterior, en el primer lunes habil del mes');
end;




