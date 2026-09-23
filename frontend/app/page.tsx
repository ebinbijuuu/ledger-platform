const ArrowUpRight = () => (
  <svg aria-hidden="true" viewBox="0 0 16 16" fill="none" className="size-4">
    <path d="M3 13 13 3M6 3h7v7" stroke="currentColor" strokeWidth="1.5" />
  </svg>
);

const ArrowRight = () => (
  <svg aria-hidden="true" viewBox="0 0 16 16" fill="none" className="size-4">
    <path d="M2 8h11M9 4l4 4-4 4" stroke="currentColor" strokeWidth="1.5" />
  </svg>
);

function ProductMark() {
  return (
    <span className="grid size-7 place-items-center border border-stone-900 bg-stone-900 text-[11px] font-semibold text-stone-50">
      L
    </span>
  );
}

function DashboardPreview() {
  const transactions = [
    { name: "Northstar Design", category: "Client payment", amount: "+£4,250.00", status: "Settled" },
    { name: "Figma, Inc.", category: "Software", amount: "−£24.00", status: "Settled" },
    { name: "British Gas", category: "Utilities", amount: "−£186.20", status: "Review" },
  ];

  return (
    <div className="overflow-hidden border border-stone-300 bg-[#f8f7f4] shadow-[0_24px_70px_-30px_rgba(28,25,23,0.32)]">
      <div className="flex h-12 items-center justify-between border-b border-stone-200 px-4 sm:px-5">
        <div className="flex items-center gap-2.5 text-sm font-medium text-stone-900">
          <ProductMark />
          <span className="hidden sm:inline">Ledger</span>
        </div>
        <div className="flex items-center gap-3">
          <span className="hidden font-mono text-[10px] uppercase tracking-[0.14em] text-stone-500 sm:inline">24 Jun 2026</span>
          <span className="size-2 rounded-full bg-emerald-600" />
          <span className="font-mono text-[10px] uppercase tracking-[0.12em] text-stone-600">Live</span>
        </div>
      </div>

      <div className="grid lg:grid-cols-[136px_1fr]">
        <aside className="hidden border-r border-stone-200 px-3 py-5 lg:block">
          <p className="mb-3 px-2 font-mono text-[9px] uppercase tracking-[0.15em] text-stone-400">Workspace</p>
          <nav aria-label="Dashboard navigation" className="space-y-1 text-xs">
            <a className="flex items-center gap-2 bg-stone-900 px-2 py-2 text-stone-50" href="#overview"><span className="size-1.5 bg-stone-50" /> Overview</a>
            <a className="flex items-center gap-2 px-2 py-2 text-stone-500" href="#accounts"><span className="size-1.5 border border-stone-400" /> Accounts</a>
            <a className="flex items-center gap-2 px-2 py-2 text-stone-500" href="#ledger"><span className="size-1.5 border border-stone-400" /> Ledger</a>
            <a className="flex items-center gap-2 px-2 py-2 text-stone-500" href="#security"><span className="size-1.5 border border-stone-400" /> Security</a>
          </nav>
        </aside>

        <div className="min-w-0 p-4 sm:p-6">
          <div className="mb-6 flex items-start justify-between gap-4">
            <div>
              <p className="font-mono text-[10px] uppercase tracking-[0.14em] text-stone-500">Good morning, Ava</p>
              <h2 className="mt-1 text-lg font-medium tracking-tight text-stone-900">Financial overview</h2>
            </div>
            <button className="border border-stone-300 bg-[#f8f7f4] px-3 py-1.5 text-xs font-medium text-stone-800" type="button">Export</button>
          </div>

          <div id="accounts" className="grid gap-px overflow-hidden border border-stone-200 bg-stone-200 sm:grid-cols-3">
            <div className="bg-[#f8f7f4] p-4"><p className="font-mono text-[9px] uppercase tracking-[0.14em] text-stone-500">Available balance</p><p className="mt-2 text-xl font-medium tracking-tight text-stone-950">£12,840.52</p><p className="mt-1 text-[11px] text-emerald-700">+8.2% this month</p></div>
            <div className="bg-[#f8f7f4] p-4"><p className="font-mono text-[9px] uppercase tracking-[0.14em] text-stone-500">Incoming</p><p className="mt-2 text-xl font-medium tracking-tight text-stone-950">£18,420.00</p><p className="mt-1 text-[11px] text-stone-500">12 payments cleared</p></div>
            <div className="bg-[#f8f7f4] p-4"><p className="font-mono text-[9px] uppercase tracking-[0.14em] text-stone-500">Protected funds</p><p className="mt-2 text-xl font-medium tracking-tight text-stone-950">£12,840.52</p><p className="mt-1 text-[11px] text-stone-500">Reconciled today</p></div>
          </div>

          <div className="mt-6 grid gap-6 xl:grid-cols-[1.45fr_0.85fr]">
            <section>
              <div className="mb-3 flex items-center justify-between"><h3 className="text-xs font-medium text-stone-900">Recent activity</h3><span className="font-mono text-[9px] uppercase tracking-[0.12em] text-stone-500">Last 7 days</span></div>
              <div className="border-y border-stone-200">
                {transactions.map((transaction) => (
                  <div className="grid grid-cols-[1fr_auto] gap-x-3 border-b border-stone-200 py-3 last:border-0" key={transaction.name}>
                    <div className="min-w-0"><p className="truncate text-xs font-medium text-stone-800">{transaction.name}</p><p className="mt-0.5 text-[10px] text-stone-500">{transaction.category} · {transaction.status}</p></div>
                    <p className={`self-center font-mono text-[11px] ${transaction.amount.startsWith("+") ? "text-emerald-700" : "text-stone-700"}`}>{transaction.amount}</p>
                  </div>
                ))}
              </div>
            </section>
            <section className="border-l border-stone-200 pl-0 xl:pl-6">
              <div className="flex items-center justify-between"><h3 className="text-xs font-medium text-stone-900">Account health</h3><span className="size-2 rounded-full bg-emerald-600" /></div>
              <div className="mt-4 space-y-3">
                <div><div className="mb-1.5 flex justify-between text-[10px] text-stone-500"><span>Records reconciled</span><span>98.4%</span></div><div className="h-1 bg-stone-200"><div className="h-full w-[98%] bg-stone-900" /></div></div>
                <div><div className="mb-1.5 flex justify-between text-[10px] text-stone-500"><span>Fraud review queue</span><span>2 items</span></div><div className="h-1 bg-stone-200"><div className="h-full w-[18%] bg-amber-600" /></div></div>
              </div>
              <p className="mt-5 border-l-2 border-emerald-600 pl-3 text-[10px] leading-4 text-stone-600">All accounts are within their normal operating range.</p>
            </section>
          </div>
        </div>
      </div>
    </div>
  );
}

export default function Home() {
  return (
    <main className="overflow-hidden bg-[#f8f7f4] text-stone-900">
      <header className="mx-auto flex max-w-7xl items-center justify-between px-6 py-5 lg:px-8">
        <a className="flex items-center gap-2.5 text-sm font-semibold tracking-tight" href="#top" aria-label="Ledger home"><ProductMark /> Ledger</a>
        <nav aria-label="Main navigation" className="hidden items-center gap-7 text-xs text-stone-600 md:flex"><a className="transition-colors hover:text-stone-950" href="#product">Product</a><a className="transition-colors hover:text-stone-950" href="#security">Security</a><a className="transition-colors hover:text-stone-950" href="#ledger">Ledger</a></nav>
        <a className="border border-stone-300 px-3.5 py-2 text-xs font-medium transition-colors hover:border-stone-900" href="#get-started">Sign in</a>
      </header>

      <section id="top" className="mx-auto max-w-7xl px-6 pb-20 pt-16 lg:px-8 lg:pb-28 lg:pt-24">
        <div className="grid items-end gap-12 lg:grid-cols-[0.83fr_1.17fr] lg:gap-16">
          <div className="max-w-xl">
            <p className="mb-5 font-mono text-[10px] uppercase tracking-[0.16em] text-stone-500">Financial infrastructure, considered</p>
            <h1 className="text-5xl font-medium leading-[0.98] tracking-[-0.055em] text-stone-950 sm:text-6xl lg:text-7xl">Built for a clearer view of money.</h1>
            <p className="mt-7 max-w-md text-base leading-7 text-stone-600">Ledger brings accounts, payments, and the records behind them into one calm, accountable workspace.</p>
            <div className="mt-9 flex flex-wrap gap-3" id="get-started"><a className="inline-flex items-center gap-2 bg-stone-900 px-4 py-3 text-xs font-medium text-stone-50 transition-colors hover:bg-stone-700" href="#product">Get started <ArrowUpRight /></a><a className="inline-flex items-center gap-2 border border-stone-300 px-4 py-3 text-xs font-medium transition-colors hover:border-stone-900" href="#overview">View demo <ArrowRight /></a></div>
            <div className="mt-14 flex items-center gap-5 border-t border-stone-300 pt-5 text-[11px] text-stone-500"><span className="font-mono uppercase tracking-[0.13em]">Built for teams</span><span className="h-3 border-l border-stone-300" /><span>Clear records. Confident decisions.</span></div>
          </div>
          <div id="overview" className="relative lg:translate-y-3"><DashboardPreview /></div>
        </div>
      </section>

      <section id="product" className="border-y border-stone-300 bg-stone-900 text-stone-50">
        <div className="mx-auto max-w-7xl px-6 py-16 lg:px-8 lg:py-20">
          <div className="grid gap-12 lg:grid-cols-[0.8fr_1.2fr] lg:gap-20">
            <div><p className="font-mono text-[10px] uppercase tracking-[0.16em] text-stone-400">The operating layer</p><h2 className="mt-4 max-w-sm text-3xl font-medium leading-tight tracking-[-0.04em] sm:text-4xl">The detail you need, without the noise.</h2></div>
            <div className="grid gap-px border border-stone-700 bg-stone-700 sm:grid-cols-3">
              {[["01", "Accounts", "See every balance, entity, and account status in a single reliable view."], ["02", "Transactions", "Follow funds from initiation to settlement with context at every step."], ["03", "Controls", "Surface unusual activity early with practical, explainable risk signals."]].map(([number, title, description]) => (
                <article className="bg-stone-900 p-5 sm:p-6" key={title}><p className="font-mono text-[10px] text-stone-400">{number}</p><h3 className="mt-10 text-base font-medium">{title}</h3><p className="mt-3 text-sm leading-6 text-stone-400">{description}</p></article>
              ))}
            </div>
          </div>
        </div>
      </section>

      <section className="mx-auto max-w-7xl px-6 py-20 lg:px-8 lg:py-28">
        <div className="grid gap-12 border-t border-stone-300 pt-6 lg:grid-cols-[0.7fr_1.3fr] lg:gap-20">
          <div><p className="font-mono text-[10px] uppercase tracking-[0.16em] text-stone-500">Designed around the record</p><h2 className="mt-4 text-3xl font-medium leading-tight tracking-[-0.04em]">A durable source of financial truth.</h2></div>
          <div className="grid gap-10 md:grid-cols-2">
            <article id="ledger"><p className="font-mono text-[10px] uppercase tracking-[0.15em] text-stone-500">Ledger integrity</p><h3 className="mt-3 text-xl font-medium tracking-tight">Every movement has a history.</h3><p className="mt-3 max-w-sm text-sm leading-6 text-stone-600">Double-entry records keep debits and credits in balance, creating an audit trail that is easy to review and hard to dispute.</p><a className="mt-5 inline-flex items-center gap-2 text-xs font-medium text-stone-900" href="#top">Explore the ledger <ArrowRight /></a></article>
            <article id="security" className="border-l border-stone-300 pl-6"><p className="font-mono text-[10px] uppercase tracking-[0.15em] text-stone-500">Risk, with context</p><h3 className="mt-3 text-xl font-medium tracking-tight">Fraud signals, made useful.</h3><p className="mt-3 max-w-sm text-sm leading-6 text-stone-600">Review unusual behaviour alongside the account and transaction context your team needs to make a measured call.</p><a className="mt-5 inline-flex items-center gap-2 text-xs font-medium text-stone-900" href="#top">See security controls <ArrowRight /></a></article>
          </div>
        </div>
      </section>

      <footer className="border-t border-stone-300"><div className="mx-auto flex max-w-7xl flex-col gap-5 px-6 py-7 text-xs text-stone-500 sm:flex-row sm:items-center sm:justify-between lg:px-8"><div className="flex items-center gap-2.5 font-medium text-stone-900"><ProductMark /> Ledger</div><p>Financial infrastructure for teams that care about the record.</p><p className="font-mono text-[10px] uppercase tracking-[0.13em]">© 2026 Ledger</p></div></footer>
    </main>
  );
}
