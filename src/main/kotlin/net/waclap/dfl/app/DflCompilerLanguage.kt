package net.waclap.dfl.app

class DflCompilerLanguage {
    internal var map: Map<String, String> = hashMapOf()

    companion object {
        val EN_US = DflCompilerLanguage()
        val JA_JP = DflCompilerLanguage()

        init {
            EN_US.map = hashMapOf(
                "error.expected" to "Expected '%s'",
                "error.reader.unknown_operation" to "Unknown operation symbol '%s'",
                "error.illegal_format" to "Illegal formatting",
                "error.invalid_symbol" to "Invalid symbol '%s'",
                "error.path.invalid" to "Invalid path '%s'",
                "error.cant_open_directory" to "'%s' is a directory",
                "error.invalid_resource_lcoation" to "Invalid resource location '%s'",
                "error.invalid_file_extension" to "Invalid file extension '%s'",
                "error.invalid_data_type_name" to "Invalid data type '%s'",
                "error.invalid_namespace_name" to "Invalid namespace '%s'",
                "error.close_underflow" to "Close operation when a file is unopened",
                "error.var_not_exist" to "Undefined variable '%s'",
                "error.expected_flat" to "Expected %s",
                "error.type_need_pop" to "Required to pop type",
                "error.unable_to_recurse" to "Unable to recurse here",
                "error.comment_unclosed" to "The comment block is not closed correctly",

                "error.jexl.expected_macro" to "Expected macro definition",
                "error.jexl.need_macro_name" to "Requires macro name",
                "error.jexl.unmatch_args" to "Unmatch args count (Expected %s but %s was given)",
                "error.jexl.macro_not_exist" to "Macro '%s' is undefined",
                "error.jexl.macro_already_exist" to "Macro '%s' is already defined",
                "error.jexl.invalid_value" to "Invalid value '%s'",
                "error.jexl.expected_value_name" to "Requires value name",
                "error.jexl.execution" to "Failed to execute macro",

                "error.generation.file_writer_error" to "Failed to write to the file",
                "error.generation.could_not_open" to "Could not open file '%s'",
                "error.generation.could_not_write" to "Could not write to file '%s'",
                "error.generation.file_stack_underflow" to "File stack underflow",
                "error.generation.no_root_directory" to "The root directory is unset"
            )

            JA_JP.map = hashMapOf(
                "error.expected" to "'%s'が見つかりません",
                "error.reader.unknown_operation" to "無効な命令シンボル'%s'",
                "error.illegal_format" to "無効なフォーマッティング",
                "error.invalid_symbol" to "無効なシンボル'%s'",
                "error.path.invalid" to "無効なパス'%s'",
                "error.cant_open_directory" to "'%s'はディレクトリです",
                "error.invalid_resource_lcoation" to "無効なリソースロケーション'%s'",
                "error.invalid_file_extension" to "無効なファイル拡張子'%s'",
                "error.invalid_data_type_name" to "無効なデータタイプ'%s'",
                "error.invalid_namespace_name" to "無効な名前空間'%s'",
                "error.close_underflow" to "ファイルを開いていない間のclose命令",
                "error.var_not_exist" to "変数'%s'は定義されていません",
                "error.expected_flat" to "%sが見つかりません",
                "error.type_need_pop" to "タイプ設定をポップする必要があります",
                "error.unable_to_recurse" to "ここでは再帰は使えません",
                "error.comment_unclosed" to "コメントブロックが閉じていません",

                "error.jexl.expected_macro" to "マクロ定義がありません",
                "error.jexl.need_macro_name" to "マクロ名が必要です",
                "error.jexl.unmatch_args" to "引数の数が一致しません(必要数:%s, 指定数: %s)",
                "error.jexl.macro_not_exist" to "マクロ'%s'は定義されていません",
                "error.jexl.macro_already_exist" to "マクロ'%s'は既に定義されています",
                "error.jexl.invalid_value" to "無効な値'%s'",
                "error.jexl.expected_value_name" to "変数名が必要です",
                "error.jexl.execution" to "マクロの実行に失敗しました",

                "error.generation.file_writer_error" to "ファイル書き込みに失敗しました",
                "error.generation.could_not_open" to "ファイル'%s'を開けませんでした",
                "error.generation.could_not_write" to "ファイル'%s'に書き込めませんでした",
                "error.generation.file_stack_underflow" to "File stack underflow",
                "error.generation.no_root_directory" to "ルートディレクトリが設定されていません"
            )
        }
    }
}