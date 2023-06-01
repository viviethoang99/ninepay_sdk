package com.npsdk.jetpack_sdk.base.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable

@Composable
fun ExpandedRow(content: @Composable RowScope.() -> Unit) {
	Row {
		content()
	}
}


@Composable
fun ExpandedColumn(content: @Composable ColumnScope.() -> Unit) {
	Column {
		content()
	}
}